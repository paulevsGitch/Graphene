package paulevs.graphene.rendering.shaders.uniforms;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

@Environment(EnvType.CLIENT)
public class TextureUniform extends Uniform {
	private int texture;
	
	public TextureUniform(int target) {
		super(target);
		GL20.glUniform1i(target, target);
	}
	
	public void setTexture(int texture) {
		this.texture = texture;
	}
	
	public void bind() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + target);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
	}
}
