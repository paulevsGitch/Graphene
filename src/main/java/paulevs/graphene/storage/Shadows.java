package paulevs.graphene.storage;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.bhcore.rendering.shaders.Shader;
import paulevs.bhcore.rendering.shaders.ShaderProgram;
import paulevs.bhcore.rendering.shaders.ShaderType;
import paulevs.bhcore.rendering.shaders.buffers.MultiBuffer;
import paulevs.bhcore.rendering.shaders.buffers.MultiBufferBuilder;
import paulevs.graphene.Graphene;

@Environment(EnvType.CLIENT)
public class Shadows {
	public ShaderProgram shaderProgram;
	public MultiBuffer shadowBuffer;
	
	public Shadows() {
		Identifier shadowID = Graphene.makeID("shadows");
		shaderProgram = new ShaderProgram(Shader.create(shadowID, ShaderType.VERTEX), Shader.create(shadowID, ShaderType.FRAGMENT));
		shadowBuffer = MultiBufferBuilder
			.start(256, 256)
			.addDepthMap()
			.build(shaderProgram);
	}
}
