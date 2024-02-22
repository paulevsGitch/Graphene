package paulevs.graphene.rendering.shaders.uniforms;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL20;

@Environment(EnvType.CLIENT)
public class BooleanUniform extends Uniform {
	private boolean value;
	
	public BooleanUniform(int target) {
		super(target);
	}
	
	public void setValue(boolean value) {
		this.value = value;
	}
	
	@Override
	public void bind() {
		GL20.glUniform1i(target, value ? 1 : 0);
	}
}
