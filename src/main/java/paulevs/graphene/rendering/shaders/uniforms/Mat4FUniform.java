package paulevs.graphene.rendering.shaders.uniforms;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

@Environment(EnvType.CLIENT)
public class Mat4FUniform extends Uniform {
	private final FloatBuffer buffer = ByteBuffer.allocateDirect(64).asFloatBuffer();
	
	public Mat4FUniform(int target) {
		super(target);
	}
	
	public void setValue(int index, float value) {
		buffer.put(index, value);
	}
	
	public void readModelMatrix() {
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, buffer);
	}
	
	public void readProjectionMatrix() {
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, buffer);
	}
	
	@Override
	public void bind() {
		GL20.glUniformMatrix4(target, false, buffer);
	}
}
