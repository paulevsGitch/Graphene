package paulevs.graphene.mixin.client;

import net.minecraft.client.render.LevelRenderer;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.graphene.Graphene;
import paulevs.graphene.rendering.shaders.Programs;
import paulevs.graphene.rendering.shaders.ShaderProgram;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
	@Shadow private Level level;
	
	@Inject(method = "renderAreas", at = @At("HEAD"))
	private void graphene_renderTerrainStart(int layerIndex, double delta, CallbackInfo info) {
		Programs.TERRAIN_PROGRAM.bind();
		Programs.TERRAIN_TIME.setValue(((float) ((int) level.getLevelTime() % 24000) + (float) delta) / 24000.0F);
		Programs.TERRAIN_TIME.bind();
		Programs.TERRAIN_FOG_PARAMS.bind();
		Programs.TERRAIN_FOG_COLOR.bind();
		Programs.TERRAIN_PROJECTION.readProjectionMatrix();
		Programs.TERRAIN_PROJECTION.bind();
		Programs.TERRAIN_MODEL_VIEW.readModelMatrix();
		Programs.TERRAIN_MODEL_VIEW.bind();
		
		LivingEntity entity = Graphene.getMinecraft().viewEntity;
		float x = (float) MathHelper.lerp(delta, entity.prevRenderX, entity.x);
		float y = (float) MathHelper.lerp(delta, entity.prevRenderY, entity.y);
		float z = (float) MathHelper.lerp(delta, entity.prevRenderZ, entity.z);
		Programs.TERRAIN_CAMERA_POS.setValue(x, y, z);
		Programs.TERRAIN_CAMERA_POS.bind();
	}
	
	@Inject(method = "renderAreas", at = @At("TAIL"))
	private void graphene_renderTerrainEnd(int layerIndex, double delta, CallbackInfo info) {
		ShaderProgram.unbind();
		//GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		//GL30.glBindVertexArray(0);
	}
}
