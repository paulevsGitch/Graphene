package paulevs.graphene.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BaseBlock;
import net.minecraft.block.LeavesBaseBlock;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bhcore.util.BlocksUtil;
import paulevs.bhcore.util.ClientUtil;
import paulevs.graphene.particles.LeafParticle;
import paulevs.graphene.storage.CustomParticles;
import paulevs.graphene.storage.CustomParticles.ParticleInfo;

import java.util.Random;

@Mixin(LeavesBaseBlock.class)
public abstract class LeavesBlockMixin extends BaseBlock {
	protected LeavesBlockMixin(int i, Material arg) {
		super(i, arg);
	}
	
	@Inject(method = "<init>", at = @At("TAIL"))
	private void onInit(int j, int arg, Material bl, boolean par4, CallbackInfo ci) {
		this.setLightOpacity(64);
	}
	
	@Environment(value= EnvType.CLIENT)
	public void randomDisplayTick(Level level, int x, int y, int z, Random random) {
		if (random.nextFloat() < LeafParticle.getChance(level) && BlocksUtil.getBlockState(level, x, y - 1, z).isAir()) {
			ParticleInfo info = CustomParticles.getInfo(BlocksUtil.getBlockState(level, x, y, z));
			if (info != null) {
				ClientUtil.getMinecraft().particleManager.addParticle(new LeafParticle(level, x, y, z, info));
			}
		}
	}
}
