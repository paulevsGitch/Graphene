package paulevs.graphene.light;

import net.minecraft.block.BaseBlock;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class LightData {
	private static final Map<BlockState, LightInfo> DATA = new HashMap<>();
	
	/**
	 * Stores information about light from specified {@link BlockState}.
	 * @param state {@link BlockState} that emits light.
	 * @param color {@code integer} color code, example: 0xFF0000 = red color.
	 * @param radius {@code byte} light radius, should be in 0-15 range.
	 */
	public static void addLightInfo(BlockState state, int color, byte radius) {
		if (radius < 0 || radius > 15) throw new RuntimeException("Light radius is out of range!");
		DATA.put(state, new LightInfo(color, radius));
	}
	
	/**
	 * Stores information about light from specified {@link BaseBlock}. All {@link BlockState}s will have identical light.
	 * @param block {@link BaseBlock} that emits light.
	 * @param color {@code integer} color code, example: 0xFF0000 = red color.
	 * @param radius {@code byte} light radius, should be in 0-15 range.
	 */
	public static void addLightInfo(BaseBlock block, int color, byte radius) {
		if (radius < 0 || radius > 15) throw new RuntimeException("Light radius is out of range!");
		LightInfo info = new LightInfo(color, radius);
		((BlockStateHolder) block).getStateManager().getStates().forEach(state -> DATA.put(state, info));
	}
	
	public static void registerVanilla() {
		addLightInfo(BaseBlock.FLOWING_LAVA, 0xFF0000, (byte) 5);
		addLightInfo(BaseBlock.STILL_LAVA, 0xFF0000, (byte) 5);
		addLightInfo(BaseBlock.GLOWSTONE, 0x00FF00, (byte) 15);
		addLightInfo(BaseBlock.TORCH, 0x0000FF, (byte) 13);
	}
	
	@Nullable
	public static LightInfo getInfo(BlockState state) {
		return DATA.get(state);
	}
	
	public static class LightInfo {
		public final int color;
		public final byte radius;
		
		public LightInfo(int color, byte radius) {
			this.color = color;
			this.radius = radius;
		}
	}
}
