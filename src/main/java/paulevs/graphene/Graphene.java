package paulevs.graphene;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import paulevs.graphene.storage.Shadows;

public class Graphene {
	public static final ModID MOD_ID = ModID.of("graphene");
	public static Shadows shadows;
	
	public static Identifier makeID(String name) {
		return MOD_ID.id(name);
	}
	
	public static void initClient() {
		shadows = new Shadows();
	}
}
