package paulevs.graphene.storage;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BaseBlock;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import paulevs.graphene.Graphene;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CustomParticles {
	private static final Map<BlockState, ParticleInfo> PARTICLES = new HashMap<>();
	
	@Nullable
	public static ParticleInfo getInfo(BlockState state) {
		return PARTICLES.get(state);
	}
	
	private static void register(BlockState state, Identifier atlas, RandomIntProvider textureIndex) {
		PARTICLES.put(state, new ParticleInfo(atlas, textureIndex));
	}
	
	private static void register(BaseBlock block, Identifier atlas, RandomIntProvider textureIndex) {
		((BlockStateHolder) block).getStateManager().getStates().forEach(state -> register(state, atlas, textureIndex));
	}
	
	private static void register(Identifier id, BlockRegistry registry, Identifier atlas, RandomIntProvider textureIndex) {
		@NotNull Optional<BaseBlock> optional = registry.get(id);
		if (optional.isPresent()) register(optional.get(), atlas, textureIndex);
	}
	
	public static void init(BlockRegistry registry) {
		Identifier leafParticles = Graphene.id("leaves.png");
		register(BaseBlock.LEAVES, leafParticles, new RandomIntProvider(3, 5));
		
		if (FabricLoader.getInstance().isModLoaded("advancedtrees")) {
			ModID modID = ModID.of("advancedtrees");
			register(modID.id("oak_leaves"), registry, leafParticles, new RandomIntProvider(0, 2));
			register(modID.id("birch_leaves"), registry, leafParticles, new RandomIntProvider(3, 5));
		}
	}
	
	public record ParticleInfo(Identifier atlas, RandomIntProvider textureIndex) {}
}
