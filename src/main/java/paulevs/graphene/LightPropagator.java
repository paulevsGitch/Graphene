package paulevs.graphene;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bhcore.storage.vector.Vec3I;
import paulevs.bhcore.util.ClientUtil;
import paulevs.bhcore.util.MathUtil;
import paulevs.graphene.storage.ChunkStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LightPropagator {
	private static final Thread LIGHT_THREAD = new Thread(() -> processLights());
	private static final Queue<LightEntry> UPDATES = new ConcurrentLinkedQueue<>();
	private static final List<List<Vec3I>> BUFFERS = new ArrayList<>(2);
	private static final boolean[] MASK = new boolean[35937];
	private static final byte MASK_OFFSET = (byte) 17;
	
	private static void processLights() {
		while (true) {
			LightEntry entry = UPDATES.poll();
			if (entry == null) continue;
			fillLight(entry.level, entry.color, entry.pos.x, entry.pos.y, entry.pos.z, entry.radius);
			synchronized (entry.level) {
				ClientUtil.updateArea(
					entry.level,
					entry.pos.x - entry.radius,
					entry.pos.y - entry.radius,
					entry.pos.z - entry.radius,
					entry.pos.x + entry.radius,
					entry.pos.y + entry.radius,
					entry.pos.z + entry.radius
				);
			}
		}
	}
	
	public static void start() {
		BUFFERS.add(new ArrayList<>(1024));
		BUFFERS.add(new ArrayList<>(1024));
		LIGHT_THREAD.setName("Graphene Light Propagator");
		LIGHT_THREAD.start();
	}
	
	public static void exitWorld() {
		UPDATES.clear();
	}
	
	public static void addLight(Level level, int x, int y, int z, byte radius, int color) {
		UPDATES.add(new LightEntry(level, new Vec3I(x, y, z), radius, color));
	}
	
	private static class LightEntry {
		final Level level;
		final Vec3I pos;
		final byte radius;
		final int color;
		
		private LightEntry(Level level, Vec3I pos, byte radius, int color) {
			this.level = level;
			this.pos = pos;
			this.radius = radius;
			this.color = color;
		}
	}
	
	private static int getMaskIndex(byte x, byte y, byte z) {
		return x * 1089 + y * 33 + z;
	}
	
	private static void fillLight(Level level, int rgb, int x, int y, int z, byte radius) {
		byte delta = (byte) (255 / radius);
		short r0 = (short) (rgb >> 16 & 255);
		short g0 = (short) (rgb >> 8 & 255);
		short b0 = (short) (rgb & 255);
		
		Arrays.fill(MASK, false);
		BlockStateView view = (BlockStateView) level;
		MASK[getMaskIndex(MASK_OFFSET, MASK_OFFSET, MASK_OFFSET)] = true;
		ChunkStorage.setLight(level, x, y, z, rgb);
		
		BUFFERS.get(0).add(new Vec3I(x, y, z));
		byte bufferIndex = 0;
		
		for (byte i = 0; i < radius; i++) {
			short d1 = (short) (delta * i);
			short r1 = (short) MathUtil.clamp(r0 - d1, 0, 255);
			short g1 = (short) MathUtil.clamp(g0 - d1, 0, 255);
			short b1 = (short) MathUtil.clamp(b0 - d1, 0, 255);
			
			List<Vec3I> starts = BUFFERS.get(bufferIndex);
			bufferIndex = (byte) ((bufferIndex + 1) & 1);
			List<Vec3I> ends = BUFFERS.get(bufferIndex);
			
			for (Vec3I start: starts) {
				for (Direction offset: MathUtil.DIRECTIONS) {
					Vec3I pos = start.clone().add(offset.vector.x, offset.vector.y, offset.vector.z);
					byte maskX = (byte) (pos.x - x);
					byte maskY = (byte) (pos.y - y);
					byte maskZ = (byte) (pos.z - z);
					if (maskX < -radius || maskY < -radius || maskZ < -radius || maskX > radius || maskY > radius || maskZ > radius) continue;
					
					maskX += MASK_OFFSET;
					maskY += MASK_OFFSET;
					maskZ += MASK_OFFSET;
					
					int maskIndex = getMaskIndex(maskX, maskY, maskZ);
					if (MASK[maskIndex]) continue;
					
					BlockState state = view.getBlockState(pos.x, pos.y, pos.z);
					if (state.isOpaque()) continue; // TODO replace with light propagation values
					
					rgb = ChunkStorage.getLight(level, pos.x, pos.y, pos.z);
					short r2 = (short) (rgb >> 16 & 255);
					short g2 = (short) (rgb >> 8 & 255);
					short b2 = (short) (rgb & 255);
					
					int rgb2 = Math.max(r1, r2) << 16 | Math.max(g1, g2) << 8 | Math.max(b1, b2);
					if (rgb != rgb2) ChunkStorage.setLight(level, pos.x, pos.y, pos.z, rgb2);
					MASK[maskIndex] = true;
					ends.add(pos);
				}
			}
			starts.clear();
		}
	}
}
