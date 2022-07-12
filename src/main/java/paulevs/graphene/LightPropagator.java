package paulevs.graphene;

import net.minecraft.level.Level;
import paulevs.bhcore.storage.vector.Vec3I;
import paulevs.graphene.storage.ChunkStorage;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LightPropagator {
	private static final Thread LIGHT_THREAD = new Thread(() -> processLights());
	private static final Queue<LightEntry> UPDATES = new ConcurrentLinkedQueue<>();
	
	private static void processLights() {
		while (true) {
			LightEntry entry = UPDATES.poll();
			if (entry == null) continue;
			for (byte i = -1; i < 2; i++) {
				int px = entry.pos.x + i;
				for (byte j = -1; j < 2; j++) {
					int py = entry.pos.y + j;
					for (byte k = -1; k < 2; k++) {
						int pz = entry.pos.z + k;
						ChunkStorage.setLight(entry.level, px, py, pz, entry.color);
					}
				}
			}
		}
	}
	
	public static void start() {
		LIGHT_THREAD.setName("Graphene Light Propagator");
		LIGHT_THREAD.start();
	}
	
	public static void exitWorld() {
		UPDATES.clear();
	}
	
	public static void addLight(Level level, Vec3I pos, byte radius, int color) {
		UPDATES.add(new LightEntry(level, pos, radius, color));
	}
	
	private static class LightEntry {
		final Level level;
		final Vec3I pos;
		final int radius;
		final int color;
		
		private LightEntry(Level level, Vec3I pos, byte radius, int color) {
			this.level = level;
			this.pos = pos;
			this.radius = radius;
			this.color = color;
		}
	}
}
