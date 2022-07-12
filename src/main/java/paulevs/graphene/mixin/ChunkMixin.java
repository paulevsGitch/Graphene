package paulevs.graphene.mixin;

import net.minecraft.level.LightType;
import net.minecraft.level.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)
public class ChunkMixin {
	@Inject(method = "fillSkyLight(II)V", at = @At("HEAD"), cancellable = true)
	private void graphene_disableFillSkyLight(int i, int j, CallbackInfo info) {
		info.cancel();
	}
	
	@Inject(method = "fillLightColumn(III)V", at = @At("HEAD"), cancellable = true)
	private void graphene_disableFillLightColumn(int i, int j, int k, CallbackInfo info) {
		info.cancel();
	}
	
	@Inject(method = "updateSkylight(III)V", at = @At("HEAD"), cancellable = true)
	private void graphene_disableUpdateSkylight(int i, int j, int k, CallbackInfo info) {
		info.cancel();
	}
	
	@Inject(method = "getLight(Lnet/minecraft/level/LightType;III)I", at = @At("HEAD"), cancellable = true)
	private void graphene_getChunkLight(LightType arg, int i, int j, int k, CallbackInfoReturnable<Integer> info) {
		info.setReturnValue(0);
	}
	
	@Inject(method = "setLight(Lnet/minecraft/level/LightType;IIII)V", at = @At("HEAD"), cancellable = true)
	private void graphene_disableSetLight(LightType arg, int i, int j, int k, int l, CallbackInfo info) {
		info.cancel();
	}
}
