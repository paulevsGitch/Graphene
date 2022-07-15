package paulevs.graphene.mixin;

import net.minecraft.block.BaseBlock;
import net.minecraft.level.Level;
import net.minecraft.level.LightType;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.impl.level.HeightLimitView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.graphene.light.LightPropagator;

@Mixin(value = Level.class, priority = 2000)
//@Implements(@Interface(iface = BlockStateView.class, prefix = "state$"))
public abstract class LevelMixin implements BlockStateView, HeightLimitView {
	@Inject(method = "updateLight(Lnet/minecraft/level/LightType;IIIIIIZ)V", at = @At("HEAD"), cancellable = true)
	private void graphene_disableLevelUpdateLight(LightType arg, int i, int j, int k, int l, int m, int n, boolean bl, CallbackInfo info) {
		info.cancel();
	}
	
	/*@Intrinsic(displace = true)
	public BlockState state$setBlockState(int x, int y, int z, BlockState blockState) {
		LightPropagator.addLight(blockState, Level.class.cast(this), x, y, z);
		return this.setBlockState(x, y, z, blockState);
	}*/
	
	@Inject(method = "setBlockInChunk", at = @At("HEAD"))
	private void graphene_setBlockInChunk(int x, int y, int z, int id, int meta, CallbackInfoReturnable<Boolean> info) {
		BaseBlock block = BaseBlock.BY_ID[id];
		LightPropagator.addLight(((BlockStateHolder) block).getDefaultState(), Level.class.cast(this), x, y, z);
	}
}
