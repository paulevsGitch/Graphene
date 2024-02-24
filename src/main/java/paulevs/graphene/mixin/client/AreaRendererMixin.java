package paulevs.graphene.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.client.render.AreaRenderer;
import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.graphene.rendering.GrapheneTessellator;
import paulevs.graphene.rendering.shaders.Programs;

@Mixin(AreaRenderer.class)
public class AreaRendererMixin {
	@Shadow public int startX;
	@Shadow public int startY;
	@Shadow public int startZ;
	
	@Inject(method = "update", at = @At(
		value = "INVOKE",
		target = "Lorg/lwjgl/opengl/GL11;glNewList(II)V",
		shift = Shift.AFTER
	))
	private void graphene_setPosition(CallbackInfo info) {
		Programs.TERRAIN_POS.setValue(startX, startY, startZ);
		((GrapheneTessellator) Tessellator.INSTANCE).graphene_setRendering(true);
	}
	
	@Inject(method = "update", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/render/Tessellator;render()V",
		shift = Shift.AFTER
	))
	private void graphene_fixUV(CallbackInfo info) {
		((GrapheneTessellator) Tessellator.INSTANCE).graphene_setRendering(false);
	}
	
	@Inject(method = "update", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/render/block/BlockRenderer;render(Lnet/minecraft/block/Block;III)Z",
		shift = Shift.BEFORE
	))
	private void graphene_setBlock(CallbackInfo info, @Local Block block) {
		((GrapheneTessellator) Tessellator.INSTANCE).graphene_setBlock(block);
	}
}
