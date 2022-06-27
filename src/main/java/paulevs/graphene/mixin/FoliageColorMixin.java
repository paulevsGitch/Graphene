package paulevs.graphene.mixin;

import net.minecraft.client.render.block.FoliageColor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FoliageColor.class)
public class FoliageColorMixin {
	/*@Inject(method = "getBirchColor()I", at = @At("HEAD"), cancellable = true)
	private static void graphene_changeBirchColor(CallbackInfoReturnable<Integer> info) {
		info.setReturnValue(0x91ca57);
	}*/
}
