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
	private final int delayAge;
	private float scale;
	
	public LeafParticle(Level level, double x, double y, double z, ParticleInfo info) {
		super(level, x, y, z, 0, 0, 0);
		this.velocityX = MathUtil.randomRange(-0.1F, 0.1F, rand);
		this.velocityZ = MathUtil.randomRange(-0.1F, 0.1F, rand);
		this.velocityY = -0.05;
		this.maxAge = 300;
		this.flipped = rand.nextBoolean();
		this.textureIndex = info.textureIndex().get(rand);
		this.atlas = info.atlas();
		this.delayAge = this.maxAge - 20;
		this.scale = 1.0F;
		
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
		if (this.age > this.delayAge) {
			float delta = (this.age - this.delayAge) / 20.0F;
			this.scale = 1.0F - delta;
		}
		super.tick();
	}
	
	@Override
	public Identifier getTextureAtlas() {
		return atlas;
	}
	
	@Override
	public void render(Tessellator tessellator, float delta, float x, float y, float z, float width, float height) {
		render(tessellator, delta, x, y, z, width, height, flipped);
	}
	
	protected void render(Tessellator tessellator, float delta, float x, float y, float z, float width, float height, boolean flipped) {
		float u1 = (float) (this.textureIndex & 15) / 16.0F;
		float u2 = u1 + 0.0624375F;
		float v1 = (float) (this.textureIndex >> 4) / 16.0F;
		float v2 = v1 + 0.0624375F;
		float side = 0.1F * this.size * scale;
		float px = (float) (this.prevX + (this.x - this.prevX) * (double) delta - posX);
		float py = (float) (this.prevY + (this.y - this.prevY) * (double) delta - posY);
		float pz = (float) (this.prevZ + (this.z - this.prevZ) * (double) delta - posZ);
		float light = this.getBrightnessAtEyes(delta);
		
		if (flipped) {
			float u = u1;
			u1 = u2;
			u2 = u;
		}
		tessellator.colour(this.colorR * light, this.colorG * light, this.colorB * light);
		tessellator.vertex(px - x * side - width * side, py - y * side, pz - z * side - height * side, u2, v2);
		tessellator.vertex(px - x * side + width * side, py + y * side, pz - z * side + height * side, u2, v1);
		tessellator.vertex(px + x * side + width * side, py + y * side, pz + z * side + height * side, u1, v1);
		tessellator.vertex(px + x * side - width * side, py - y * side, pz + z * side - height * side, u1, v2);
	}
	
	public static float getChance(Level level) {
		return level.isRaining() ? 0.02F : 0.002F;
	}
}
