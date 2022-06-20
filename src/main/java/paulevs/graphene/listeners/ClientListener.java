package paulevs.graphene.listeners;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.registry.AfterBlockAndItemRegisterEvent;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import paulevs.bhcore.util.ModelUtil;
import paulevs.graphene.Graphene;

public class ClientListener {
	@EventListener
	public void afterBlocksInit(AfterBlockAndItemRegisterEvent event) {
		ModelUtil.registerReplacement(Identifier.of("block/leaves"), Graphene.makeID("block/oak_leaves"));
		
		if (FabricLoader.getInstance().isModLoaded("advancedtrees")) {
			ModID id = ModID.of("advancedtrees");
			ModelUtil.registerReplacement(id.id("block/birch_leaves"), Graphene.makeID("block/birch_leaves"));
			ModelUtil.registerReplacement(id.id("block/oak_leaves"), Graphene.makeID("block/oak_leaves"));
		}
	}
}
