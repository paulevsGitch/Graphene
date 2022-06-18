package paulevs.graphene.mixin;

import net.minecraft.client.render.block.FoliageColour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FoliageColour.class)
public class FoliageColourMixin {
	@Inject(method = "getBirchColor()I", at = @At("HEAD"), cancellable = true)
	private static void graphene_changeBirchColor(CallbackInfoReturnable<Integer> info) {
		info.setReturnValue(0x91ca57);
	}
}
