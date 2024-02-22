package paulevs.graphene.rendering.shaders;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL20;

@Environment(EnvType.CLIENT)
public enum ShaderType {
	VERTEX(GL20.GL_VERTEX_SHADER, ".vert"),
	FRAGMENT(GL20.GL_FRAGMENT_SHADER, ".frag");
	
	private final int glType;
	private final String extension;
	
	ShaderType(int glType, String extension) {
		this.glType = glType;
		this.extension = extension;
	}
	
	public int getGLType() {
		return glType;
	}
	
	public String getExtension() {
		return extension;
	}
}
