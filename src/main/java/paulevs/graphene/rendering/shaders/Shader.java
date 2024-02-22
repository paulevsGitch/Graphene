package paulevs.graphene.rendering.shaders;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.Identifier;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import paulevs.graphene.rendering.disposing.Disposable;
import paulevs.graphene.rendering.disposing.DisposeUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Environment(EnvType.CLIENT)
public class Shader implements Disposable {
	private final int target;
	private boolean disposed;
	
	public Shader(String shaderCode, ShaderType type) {
		target = GL20.glCreateShader(type.getGLType());
		GL20.glShaderSource(target, shaderCode);
		GL20.glCompileShader(target);
		if (GL20.glGetShaderi(target, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			throw new RuntimeException(
				"Can't create shader with type " +
				type + ", reason: " + GL20.glGetShaderInfoLog(target, 512)
			);
		}
		DisposeUtil.add(this);
	}
	
	public int getTarget() {
		return target;
	}
	
	@Override
	public void dispose() {
		if (disposed) return;
		GL20.glDeleteShader(target);
		disposed = true;
	}
	
	public static Shader create(Identifier id, ShaderType type) {
		String path = "/assets/" + id.namespace + "/shaders/" + id.path + type.getExtension();
		InputStream stream = Shader.class.getResourceAsStream(path);
		if (stream == null) {
			throw new RuntimeException("No shader with path " + path);
		}
		String source = load(stream);
		return new Shader(source, type);
	}
	
	private static String load(InputStream stream) {
		StringBuilder builder = new StringBuilder();
		String line;
		
		InputStreamReader streamReader = new InputStreamReader(stream);
		BufferedReader bufferedReader = new BufferedReader(streamReader);
		
		try {
			while ((line = bufferedReader.readLine()) != null) {
				builder.append(line);
				builder.append('\n');
			}
			bufferedReader.close();
			streamReader.close();
			stream.close();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return builder.toString();
	}
}
