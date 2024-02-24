package paulevs.graphene.rendering.shaders.uniforms;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL20;

@Environment(EnvType.CLIENT)
public class Vec2FUniform extends Uniform {
	private float x;
	private float y;
	
	public Vec2FUniform(int target) {
		super(target);
	}
	
	public void setValue(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	@Override
	public void bind() {
		GL20.glUniform2f(target, x, y);
	}
}
