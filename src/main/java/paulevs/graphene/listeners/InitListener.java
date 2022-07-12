package paulevs.graphene.listeners;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import paulevs.graphene.LightPropagator;
import paulevs.graphene.storage.ChunkStorage;

public class InitListener {
	@EventListener
	public void onInit(InitEvent event) {
		ChunkStorage.init();
		LightPropagator.start();
	}
}
