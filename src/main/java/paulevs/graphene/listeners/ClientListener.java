package paulevs.graphene.listeners;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.resource.TexturePackLoadedEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import org.lwjgl.opengl.GL11;
import paulevs.graphene.Graphene;
import paulevs.graphene.rendering.shaders.Programs;
import paulevs.graphene.rendering.shaders.ShaderProgram;

public class ClientListener {
	@EventListener
	public void onAssetsReload(TextureRegisterEvent event) {
		int texture = Graphene.getMinecraft().textureManager.getTextureId("/assets/graphene/textures/wind_map.png");
		Programs.TERRAIN_PROGRAM.bind();
		Programs.TERRAIN_WIND.setTexture(texture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		ShaderProgram.unbind();
	}
}
