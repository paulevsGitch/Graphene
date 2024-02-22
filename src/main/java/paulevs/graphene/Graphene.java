package paulevs.graphene;

import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

public class Graphene {
	public static final Namespace NAMESPACE = Namespace.of("graphene");
	
	public static Identifier id(String name) {
		return NAMESPACE.id(name);
	}
}
