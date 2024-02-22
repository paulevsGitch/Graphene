package paulevs.graphene.rendering.shaders.uniforms;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL20;
import paulevs.graphene.rendering.shaders.texture.Texture2D;

@Environment(EnvType.CLIENT)
public class TextureUniform extends Uniform {
	private Texture2D texture;
	
	public TextureUniform(int target) {
		super(target);
		GL20.glUniform1i(target, target);
	}
	
	public void setTexture(Texture2D texture) {
		this.texture = texture;
	}
	
	public void bind() {
		if (target == -1 || texture == null) return;
		texture.bind(target);
	}
}
