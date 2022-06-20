package paulevs.graphene.mixin;

import net.minecraft.client.render.block.FoliageColour;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FoliageColour.class)
public class FoliageColourMixin {
	/*@Inject(method = "getBirchColor()I", at = @At("HEAD"), cancellable = true)
	private static void graphene_changeBirchColor(CallbackInfoReturnable<Integer> info) {
		info.setReturnValue(0x91ca57);
	}*/
}
