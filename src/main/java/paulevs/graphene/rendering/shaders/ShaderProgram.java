package paulevs.graphene.rendering.shaders;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.Identifier;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import paulevs.graphene.rendering.disposing.Disposable;
import paulevs.graphene.rendering.disposing.DisposeUtil;
import paulevs.graphene.rendering.shaders.uniforms.Uniform;

import java.util.Map;
import java.util.function.IntFunction;

@Environment(EnvType.CLIENT)
public class ShaderProgram implements Disposable {
	private final Map<String, Uniform> uniforms = new Object2ObjectOpenHashMap<>();
	private final Shader[] shaders;
	private final int target;
	
	public ShaderProgram(Shader... shaders) {
		this.shaders = shaders;
		target = GL20.glCreateProgram();
		for (Shader shader: shaders) {
			GL20.glAttachShader(target, shader.getTarget());
		}
		
		GL20.glLinkProgram(target);
		if (GL20.glGetProgrami(target, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			int logLength = GL20.glGetProgrami(target, GL20.GL_INFO_LOG_LENGTH);
			throw new RuntimeException("Can't link shader program, reason: " + GL20.glGetProgramInfoLog(target, logLength));
		}
		
		for (Shader shader: shaders) {
			GL20.glDeleteShader(shader.getTarget());
		}
		
		GL20.glValidateProgram(target);
		if (GL20.glGetProgrami(target, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
			int logLength = GL20.glGetProgrami(target, GL20.GL_INFO_LOG_LENGTH);
			throw new RuntimeException("Can't validate shader program, reason: " + GL20.glGetProgramInfoLog(target, logLength));
		}
		
		unbind();
		DisposeUtil.add(this);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Uniform> T getUniform(String name, IntFunction<T> constructor) {
		Uniform uniform = uniforms.get(name);
		if (uniform != null) {
			return (T) uniform;
		}
		this.bind();
		int id = GL20.glGetUniformLocation(this.target, name);
		T result = constructor.apply(id);
		uniforms.put(name, result);
		return result;
	}
	
	public int getAttributeLocation(String name) {
		return GL20.glGetAttribLocation(target, name);
	}
	
	public void bind() {
		GL20.glUseProgram(target);
	}
	
	public static void unbind() {
		GL20.glUseProgram(0);
	}
	
	@Override
	public void dispose() {
		for (Shader shader: shaders) {
			shader.dispose();
		}
		GL20.glDeleteProgram(target);
	}
	
	public static ShaderProgram create(Identifier id) {
		Shader vertex = Shader.create(id, ShaderType.VERTEX);
		Shader fragment = Shader.create(id, ShaderType.FRAGMENT);
		return new ShaderProgram(vertex, fragment);
	}
}
