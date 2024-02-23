package paulevs.graphene.rendering.shaders;

import paulevs.graphene.Graphene;
import paulevs.graphene.rendering.shaders.uniforms.FloatUniform;
import paulevs.graphene.rendering.shaders.uniforms.TextureUniform;
import paulevs.graphene.rendering.shaders.uniforms.Vec3FUniform;

public class Programs {
	public static final ShaderProgram TERRAIN_PROGRAM = ShaderProgram.create(Graphene.id("terrain"));
	
	public static final FloatUniform TERRAIN_TIME = TERRAIN_PROGRAM.getUniform("uTime", FloatUniform::new);
	public static final Vec3FUniform TERRAIN_POS = TERRAIN_PROGRAM.getUniform("uWorldPos", Vec3FUniform::new);
	public static final TextureUniform TERRAIN_PROPERTIES = TERRAIN_PROGRAM.getUniform("uProperties", TextureUniform::new);
	
	public static void init() {}
}