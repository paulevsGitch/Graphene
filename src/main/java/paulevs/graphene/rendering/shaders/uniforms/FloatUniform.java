package paulevs.graphene.rendering.shaders.uniforms;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL20;

@Environment(EnvType.CLIENT)
public class FloatUniform extends Uniform {
	private float value;
	
	public FloatUniform(int target) {
		super(target);
	}
	
	public void setValue(float value) {
		this.value = value;
	}
	
	@Override
	public void bind() {
		GL20.glUniform1f(target, value);
	}
}
