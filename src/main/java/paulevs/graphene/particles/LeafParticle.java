package paulevs.graphene.particles;

import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.BaseParticle;
import net.minecraft.level.Level;
import net.minecraft.util.maths.BlockPos;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.bhcore.interfaces.CustomParticle;
import paulevs.bhcore.util.BlocksUtil;
import paulevs.bhcore.util.MathUtil;
import paulevs.graphene.storage.CustomParticles.ParticleInfo;

public class LeafParticle extends BaseParticle implements CustomParticle {
	private final Identifier atlas;
	private final boolean flipped;
	
	public LeafParticle(Level level, double x, double y, double z, ParticleInfo info) {
		super(level, x, y, z, 0, 0, 0);
		this.velocityX = MathUtil.randomRange(-0.1F, 0.1F, rand);
		this.velocityZ = MathUtil.randomRange(-0.1F, 0.1F, rand);
		this.velocityY = -0.05;
		this.maxAge = 300;
		this.flipped = rand.nextBoolean();
		this.textureIndex = info.textureIndex().get(rand);
		this.atlas = info.atlas();
		
		BlockPos pos = new BlockPos(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
		BlockState state = BlocksUtil.getBlockState(level, pos.x, pos.y, pos.z);
		int color = StationRenderAPI.getBlockColours().getColour(state, level, pos);
		if (color != -1) {
			this.colorR = ((color >> 16) & 255) / 255F;
			this.colorG = ((color >> 8) & 255) / 255F;
			this.colorB = (color & 255) / 255F;
		}
	}
	
	@Override
	public void tick() {
		this.velocityY = -0.05;
		super.tick();
	}
	
	@Override
	public Identifier getTextureAtlas() {
		return atlas;
	}
	
	@Override
	public void render(Tessellator tessellator, float delta, float x, float y, float z, float width, float height) {
		if (flipped) renderFlipped(tessellator, delta, x, y, z, width, height);
		else super.render(tessellator, delta, x, y, z, width, height);
	}
	
	protected void renderFlipped(Tessellator tessellator, float delta, float x, float y, float z, float width, float height) {
		float f2 = (float) (this.textureIndex % 16) / 16.0F;
		float f3 = f2 + 0.0624375F;
		float f4 = (float) (this.textureIndex / 16) / 16.0F;
		float f5 = f4 + 0.0624375F;
		float f6 = 0.1F * this.size;
		float f7 = (float) (this.prevX + (this.x - this.prevX) * (double) delta - posX);
		float f8 = (float) (this.prevY + (this.y - this.prevY) * (double) delta - posY);
		float f9 = (float) (this.prevZ + (this.z - this.prevZ) * (double) delta - posZ);
		float f10 = this.getBrightnessAtEyes(delta);
		tessellator.colour(this.colorR * f10, this.colorG * f10, this.colorB * f10);
		tessellator.vertex(f7 - x * f6 - width * f6, f8 - y * f6, f9 - z * f6 - height * f6, f2, f5);
		tessellator.vertex(f7 - x * f6 + width * f6, f8 + y * f6, f9 - z * f6 + height * f6, f2, f4);
		tessellator.vertex(f7 + x * f6 + width * f6, f8 + y * f6, f9 + z * f6 + height * f6, f3, f4);
		tessellator.vertex(f7 + x * f6 - width * f6, f8 - y * f6, f9 + z * f6 - height * f6, f3, f5);
	}
}
