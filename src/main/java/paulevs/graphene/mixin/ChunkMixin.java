package paulevs.graphene.mixin;

import net.minecraft.level.Level;
import net.minecraft.level.LightType;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSectionsAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)
//@Implements(@Interface(iface = BlockStateView.class, prefix = "state$"))
public abstract class ChunkMixin implements ChunkSectionsAccessor, BlockStateView {
	@Shadow @Final public int x;
	@Shadow @Final public int z;
	@Shadow public Level level;
	
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
	
	/*@Intrinsic(displace = false)
	public BlockState state$setBlockState(int x, int y, int z, BlockState blockState) {
		System.out.println(blockState);
		if (blockState.getBlock() == BaseBlock.TORCH) {
			System.out.println((x | (this.x << 4)) + " " + y + " " + (z | (this.z << 4)));
		}
		LightPropagator.addLight(blockState, this.level, x | (this.x << 4), y, z | (this.z << 4));
		return setBlockState(x, y, z, blockState);
	}*/
	
	/*@Inject(method = "onChunkLoaded", at = @At("HEAD"), cancellable = true)
	private void graphene_onChunkLoaded(CallbackInfo info) {
		ChunkSection[] sections = getSections();
		int sx = this.x << 4;
		int sz = this.z << 4;
		for (ChunkSection section: sections) {
			if (section == null) continue;
			for (byte x = 0; x < 16; x++) {
				for (byte y = 0; y < 16; y++) {
					for (byte z = 0; z < 16; z++) {
						BlockState state = section.getBlockState(x, y, z);
						LightPropagator.addLight(state, this.level, x | sx, y, z | sz);
					}
				}
			}
		}
	}*/
}
