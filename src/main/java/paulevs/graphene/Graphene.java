package paulevs.graphene;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

public class Graphene {
	public static final Namespace NAMESPACE = Namespace.of("graphene");
	
	public static Identifier id(String name) {
		return NAMESPACE.id(name);
	}
	
	@Environment(EnvType.CLIENT)
	@SuppressWarnings("deprecation")
	public static Minecraft getMinecraft() {
		return (Minecraft) FabricLoader.getInstance().getGameInstance();
	}
}
