package paulevs.graphene.listeners;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.registry.AfterBlockAndItemRegisterEvent;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import paulevs.bhcore.util.ModelUtil;
import paulevs.graphene.Graphene;
import paulevs.graphene.storage.CustomParticles;

public class ClientListener {
	@EventListener
	public void afterBlocksInit(AfterBlockAndItemRegisterEvent event) {
		ModelUtil.registerReplacement(Identifier.of("block/leaves"), Graphene.id("block/birch_leaves"));
		
		if (FabricLoader.getInstance().isModLoaded("advancedtrees")) {
			ModID modID = ModID.of("advancedtrees");
			ModelUtil.registerReplacement(modID.id("block/birch_leaves"), Graphene.id("block/birch_leaves"));
			ModelUtil.registerReplacement(modID.id("block/oak_leaves"), Graphene.id("block/oak_leaves"));
			
			ModelUtil.registerReplacement(modID.id("block/birch_stem"), Graphene.id("block/birch_stem"));
			ModelUtil.registerReplacement(modID.id("block/birch_log"), Graphene.id("block/birch_log"));
			for (byte i = 1; i < 6; i++) {
				ModelUtil.registerReplacement(modID.id("block/birch_log_" + i), Graphene.id("block/birch_log_" + i));
			}
		}
		
		CustomParticles.init(BlockRegistry.INSTANCE);
	}
}
