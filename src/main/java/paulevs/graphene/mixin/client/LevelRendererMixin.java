package paulevs.graphene.mixin.client;

import net.minecraft.client.render.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.graphene.rendering.shaders.Programs;
import paulevs.graphene.rendering.shaders.ShaderProgram;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
	@Inject(method = "renderAreas", at = @At("HEAD"))
	private void graphene_renderTerrainStart(int layerIndex, double delta, CallbackInfo info) {
		Programs.TERRAIN_PROGRAM.bind();
	}
	
	@Inject(method = "renderAreas", at = @At("TAIL"))
	private void graphene_renderTerrainEnd(int layerIndex, double delta, CallbackInfo info) {
		ShaderProgram.unbind();
	}
}
