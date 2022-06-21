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
import paulevs.advancedtrees.blocks.ATLeavesBlock;
import paulevs.bhcore.util.BlocksUtil;
import paulevs.bhcore.util.ClientUtil;
import paulevs.graphene.particles.LeafParticle;
import paulevs.graphene.storage.CustomParticles;
import paulevs.graphene.storage.CustomParticles.ParticleInfo;

import java.util.Random;

@Mixin(ATLeavesBlock.class)
public abstract class ATLeavesBlockMixin extends BaseBlock {
	protected ATLeavesBlockMixin(int i, Material arg) {
		super(i, arg);
	}
	
	@Environment(value= EnvType.CLIENT)
	public void randomDisplayTick(Level level, int x, int y, int z, Random random) {
		if (random.nextInt(256) == 0 && BlocksUtil.getBlockState(level, x, y - 1, z).isAir()) {
			ParticleInfo info = CustomParticles.getInfo(BlocksUtil.getBlockState(level, x, y, z));
			if (info != null) {
				ClientUtil.getMinecraft().particleManager.addParticle(new LeafParticle(level, x, y, z, info));
			}
		}
	}
}
