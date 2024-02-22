package paulevs.graphene.rendering.shaders.uniforms;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL20;

@Environment(EnvType.CLIENT)
public class Vec3FUniform extends Uniform {
	private float x;
	private float y;
	private float z;
	
	public Vec3FUniform(int target) {
		super(target);
	}
	
	public void setValue(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void getValue(float[] out) {
		out[0] = x;
		out[1] = y;
		out[2] = z;
	}
	
	@Override
	public void bind() {
		GL20.glUniform3f(target, x, y, z);
	}
}
