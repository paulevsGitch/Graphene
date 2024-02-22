package paulevs.graphene.rendering.shaders.uniforms;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public abstract class Uniform {
	protected final int target;
	
	public Uniform(int target) {
		this.target = target;
	}
	
	public abstract void bind();
}
