package paulevs.graphene.mixin.client;

import net.minecraft.client.render.LevelRenderer;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.graphene.rendering.shaders.Programs;
import paulevs.graphene.rendering.shaders.ShaderProgram;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
	@Shadow private Level level;
	
	@Inject(method = "renderAreas", at = @At("HEAD"))
	private void graphene_renderTerrainStart(int layerIndex, double delta, CallbackInfo info) {
		Programs.TERRAIN_PROGRAM.bind();
		Programs.TERRAIN_TIME.setValue(((float) ((int) level.getLevelTime() % 24000) + (float) delta) / 24000.0F);
	}
	
	@Inject(method = "renderAreas", at = @At("TAIL"))
	private void graphene_renderTerrainEnd(int layerIndex, double delta, CallbackInfo info) {
		ShaderProgram.unbind();
	}
}
