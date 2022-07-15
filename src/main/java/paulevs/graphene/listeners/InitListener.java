package paulevs.graphene.listeners;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.registry.AfterBlockAndItemRegisterEvent;
import paulevs.graphene.light.LightData;
import paulevs.graphene.light.LightPropagator;
import paulevs.graphene.storage.ChunkStorage;

public class InitListener {
	@EventListener
	public void onInit(InitEvent event) {
		ChunkStorage.init();
		LightPropagator.start();
	}
	
	@EventListener
	public void afterBlockInit(AfterBlockAndItemRegisterEvent event) {
		LightData.registerVanilla();
	}
}
