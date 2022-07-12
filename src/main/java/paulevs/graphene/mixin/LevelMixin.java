package paulevs.graphene.mixin;

import net.minecraft.level.Level;
import net.minecraft.level.LightType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public class LevelMixin {
	@Inject(method = "updateLight(Lnet/minecraft/level/LightType;IIIIIIZ)V", at = @At("HEAD"), cancellable = true)
	private void graphene_disableLevelUpdateLight(LightType arg, int i, int j, int k, int l, int m, int n, boolean bl, CallbackInfo info) {
		info.cancel();
	}
}
