package paulevs.graphene.mixin;

import net.minecraft.block.BaseBlock;
import net.minecraft.block.LeavesBaseBlock;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBaseBlock.class)
public abstract class LeavesBlockMixin extends BaseBlock {
	protected LeavesBlockMixin(int i, Material arg) {
		super(i, arg);
	}
	
	@Inject(method = "<init>", at = @At("TAIL"))
	private void onInit(int j, int arg, Material bl, boolean par4, CallbackInfo ci) {
		this.setLightOpacity(64);
	}
}
