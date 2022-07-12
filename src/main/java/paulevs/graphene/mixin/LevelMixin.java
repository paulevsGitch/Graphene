package paulevs.graphene.mixin;

import net.minecraft.block.BaseBlock;
import net.minecraft.level.Level;
import net.minecraft.level.LightType;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.impl.level.HeightLimitView;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bhcore.util.LogUtil;
import paulevs.graphene.storage.ChunkStorage;

@Mixin(value = Level.class, priority = 2000)
@Implements(@Interface(iface = BlockStateView.class, prefix = "state$"))
public abstract class LevelMixin implements BlockStateView, HeightLimitView {
	@Shadow public abstract Chunk getChunk(int x, int z);
	
	@Inject(method = "updateLight(Lnet/minecraft/level/LightType;IIIIIIZ)V", at = @At("HEAD"), cancellable = true)
	private void graphene_disableLevelUpdateLight(LightType arg, int i, int j, int k, int l, int m, int n, boolean bl, CallbackInfo info) {
		info.cancel();
	}
	
	//@Unique
	//@Override
	//@Overwrite
	//public BlockState setBlockState(int x, int y, int z, BlockState blockState) {
	//	LogUtil.log(x,y,z);
	//	return null;
		/*if (blockState.getLuminance() > 0) {
			LogUtil.log("light!");
		}
		return ((BlockStateView) this.getChunk(x, z)).setBlockState(x & 15, y, z & 15, blockState);*/
		//return blockState;
	//}
	
	@Intrinsic(displace = true)
	public BlockState state$setBlockState(int x, int y, int z, BlockState blockState) {
		// Call original accessor
		LogUtil.log(x,y,z);
		if (blockState.getLuminance() > 0) {
			LogUtil.log("Has light!");
		}
		return this.setBlockState(x, y, z, blockState);
	}
	
	/*@Inject(method = "setBlockAndUpdate", at = @At("HEAD"))
	private void graphene_setBlockAndUpdate(int x, int y, int z, int id, CallbackInfo info) {
		BaseBlock block = BaseBlock.BY_ID[id];
		if (block == null) return;
		BlockStateHolder holder = (BlockStateHolder) block;
		BlockState state = holder.getDefaultState();
		if (state.getLuminance() > 0) LogUtil.log(state);
	}*/
	
	@Inject(method = "setBlockInChunk", at = @At("HEAD"))
	private void graphene_setBlockInChunk(int x, int y, int z, int id, int meta, CallbackInfoReturnable<Boolean> info) {
		BaseBlock block = BaseBlock.BY_ID[id];
		if (block == null || BaseBlock.EMITTANCE[id] == 0) return;
		for (byte i = -1; i < 2; i++) {
			int px = x + i;
			for (byte j = -1; j < 2; j++) {
				int py = y + j;
				for (byte k = -1; k < 2; k++) {
					int pz = z + k;
					ChunkStorage.setLight(Level.class.cast(this), px, py, pz, 0xFF0000);
				}
			}
		}
	}
	
	//@SuppressWarnings({"InvalidMemberReference", "UnresolvedMixinReference", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
	/*@Inject(method = "setBlockState", at = @At("HEAD"), remap = false)
	private void graphene_setBlockState(int x, int y, int z, BlockState blockState, CallbackInfoReturnable<BlockState> info) {
		LogUtil.log(x,y,z);
	}*/
}
