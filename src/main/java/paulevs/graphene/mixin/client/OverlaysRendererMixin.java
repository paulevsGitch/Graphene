package paulevs.graphene.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.OverlaysRenderer;
import net.minecraft.entity.living.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OverlaysRenderer.class)
public class OverlaysRendererMixin {
	@WrapOperation(method = "renderOverlays", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/entity/living/player/AbstractClientPlayer;isInFluid(Lnet/minecraft/block/material/Material;)Z"
	))
	private boolean graphene_disableWater(AbstractClientPlayer player, Material material, Operation<Boolean> operation) {
		return false;
	}
}
