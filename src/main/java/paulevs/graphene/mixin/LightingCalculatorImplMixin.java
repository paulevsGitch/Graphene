package paulevs.graphene.mixin;

import net.minecraft.level.BlockView;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.aocalc.LightingCalculatorImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bhcore.storage.vector.Vec3F;
import paulevs.bhcore.util.MathUtil;

@Mixin(value = LightingCalculatorImpl.class, remap = false)
public class LightingCalculatorImplMixin {
	@Shadow private int x, y, z;
	//@Shadow @Final public float[] light;
	//@Shadow private BlockView blockView;
	
	@Unique private double[] vertexData = new double[12];
	@Unique private static final Vec3F[] OFFSETS = new Vec3F[] {
		new Vec3F(-1, -1, -1).normalize(),
		new Vec3F( 1, -1, -1).normalize(),
		new Vec3F(-1,  1, -1).normalize(),
		new Vec3F( 1,  1, -1).normalize(),
		new Vec3F(-1, -1,  1).normalize(),
		new Vec3F( 1, -1,  1).normalize(),
		new Vec3F(-1,  1,  1).normalize(),
		new Vec3F( 1,  1,  1).normalize(),
		new Vec3F(-1,  0,  0),
		new Vec3F( 1,  0,  0),
		new Vec3F( 0, -1,  0),
		new Vec3F( 0,  1,  0),
		new Vec3F( 0,  0, -1),
		new Vec3F( 0,  0,  1)
	};
	
	@Inject(method = "calculateForQuad", at = @At("HEAD"), cancellable = true)
	private void test_calculateForQuad(BakedQuad q, CallbackInfo info) {
		info.cancel();
		calculateForQuad(
			q.getFace(),
			x + MathUtil.clamp(Float.intBitsToFloat(q.getVertexData()[0]), 0.0F, 1.0F),
			y + MathUtil.clamp(Float.intBitsToFloat(q.getVertexData()[1]), 0.0F, 1.0F),
			z + MathUtil.clamp(Float.intBitsToFloat(q.getVertexData()[2]), 0.0F, 1.0F),
			x + MathUtil.clamp(Float.intBitsToFloat(q.getVertexData()[8]), 0.0F, 1.0F),
			y + MathUtil.clamp(Float.intBitsToFloat(q.getVertexData()[9]), 0.0F, 1.0F),
			z + MathUtil.clamp(Float.intBitsToFloat(q.getVertexData()[10]), 0.0F, 1.0F),
			x + MathUtil.clamp(Float.intBitsToFloat(q.getVertexData()[16]), 0.0F, 1.0F),
			y + MathUtil.clamp(Float.intBitsToFloat(q.getVertexData()[17]), 0.0F, 1.0F),
			z + MathUtil.clamp(Float.intBitsToFloat(q.getVertexData()[18]), 0.0F, 1.0F),
			x + MathUtil.clamp(Float.intBitsToFloat(q.getVertexData()[24]), 0.0F, 1.0F),
			y + MathUtil.clamp(Float.intBitsToFloat(q.getVertexData()[25]), 0.0F, 1.0F),
			z + MathUtil.clamp(Float.intBitsToFloat(q.getVertexData()[26]), 0.0F, 1.0F),
			q.hasShade()
		);
	}
	
	@Shadow
	private void calculateForQuad(
		Direction face,
		double v00x, double v00y, double v00z,
		double v01x, double v01y, double v01z,
		double v11x, double v11y, double v11z,
		double v10x, double v10y, double v10z,
		boolean shade
	) {}
	
	/*@Shadow
	private float light(int x, int y, int z) { return 0F; }
	
	@Shadow
	private int id(int x, int y, int z) { return 0; }
	
	@Inject(method = "quadSmooth", at = @At("HEAD"), cancellable = true)
	private void quadSmooth(Direction face, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, CallbackInfo info) {
		//vertexData[0] = x1;  vertexData[1] = y1;  vertexData[2] = z1;
		//vertexData[3] = x2;  vertexData[4] = y2;  vertexData[5] = z2;
		//vertexData[6] = x3;  vertexData[7] = y3;  vertexData[8] = z3;
		//vertexData[9] = x4; vertexData[10] = y4; vertexData[11] = z4;
		
		info.cancel();
		
		this.light[0] = getLight(face, x1, y1, z1);
		this.light[1] = getLight(face, x2, y2, z2);
		this.light[2] = getLight(face, x3, y3, z3);
		this.light[3] = getLight(face, x4, y4, z4);
	}*/
	
	/*private float getLight(Direction face, double x, double y, double z) {
		float light = 0;
		for (Vec3F offset: OFFSETS) {
			light += blockView.getBrightness(
				MathHelper.floor(x + offset.x + 0.5F),
				MathHelper.floor(y + offset.y + 0.5F),
				MathHelper.floor(z + offset.z + 0.5F)
			);
		}
		light = light / (float) OFFSETS.length; */
		// return MathUtil.clamp(light * 4.0F - 1.5F, 0, 1);
		
		//return blockView.getBrightness(this.x + face.vector.x, this.y + face.vector.y, this.z + face.vector.z);
		
		/*int x1 = this.x;
		int y1 = this.y;
		int z1 = this.z;
		
		float dx = (float) (x - this.x);
		float dy = (float) (y - this.y);
		float dz = (float) (z - this.z);
		
		if ((face.axis != Axis.X && dx < 0.5F) || (face.direction == AxisDirection.NEGATIVE && dx > 0.5F)) x1--;
		if ((face.axis != Axis.Y && dy < 0.5F) || (face.direction == AxisDirection.NEGATIVE && dy > 0.5F)) y1--;
		if ((face.axis != Axis.Z && dz < 0.5F) || (face.direction == AxisDirection.NEGATIVE && dz > 0.5F)) z1--;
		
		int x2 = x1 + 1;
		int y2 = y1 + 1;
		int z2 = z1 + 1;*/
		
		//dx = 1F - (float) (x2 - x);//(float) (x - x1);
		//dy = 1F - (float) (y2 - y);//(float) (y - y1);
		//dz = 1F - (float) (z2 - z);//(float) (z - z1);
		
		//LogUtil.log(dx, dy, dz);
		
		/*float dx = (float) (x - this.x);
		float dy = (float) (y - this.y);
		float dz = (float) (z - this.z);*/
		
		/*dx = (float) (x - x1);
		dy = (float) (y - y1);
		dz = (float) (z - z1);*/
		
		/*dx = (float) (x - x1) * 0.5F;
		dy = (float) (y - y1) * 0.5F;
		dz = (float) (z - z1) * 0.5F;
		
		float l1 = blockView.getBrightness(x1, y1, z1);
		float l2 = blockView.getBrightness(x2, y1, z1);
		float l3 = blockView.getBrightness(x1, y2, z1);
		float l4 = blockView.getBrightness(x2, y2, z1);
		float l5 = blockView.getBrightness(x1, y1, z2);
		float l6 = blockView.getBrightness(x2, y1, z2);
		float l7 = blockView.getBrightness(x1, y2, z2);
		float l8 = blockView.getBrightness(x2, y2, z2);*/
		
		/*l1 = MathUtil.lerp(l1, l2, dx);
		l2 = MathUtil.lerp(l3, l4, dx);
		l3 = MathUtil.lerp(l5, l6, dx);
		l4 = MathUtil.lerp(l7, l8, dx);
		
		l1 = MathUtil.lerp(l1, l2, dy);
		l2 = MathUtil.lerp(l3, l4, dy);
		
		return MathUtil.lerp(l1, l2, dz);*/
		
		/*int x1 = this.x;//MathHelper.floor(x);
		int y1 = this.y;//MathHelper.floor(y);
		int z1 = this.z;//MathHelper.floor(z);
		
		float dx = (float) (x - x1);
		float dy = (float) (y - y1);
		float dz = (float) (z - z1);
		
		switch (face.axis) {
			case X -> {
				if (dx < 0.5F || face.direction == AxisDirection.NEGATIVE) x1--;
				if (dy < 0.5F) y1--;
				if (dz < 0.5F) z1--;
			}
			case Y -> {
				if (dy < 0.5F || face.direction == AxisDirection.NEGATIVE) y1--;
				if (dx < 0.5F) x1--;
				if (dz < 0.5F) z1--;
			}
			case Z -> {
				if (dz < 0.5F || face.direction == AxisDirection.NEGATIVE) z1--;
				if (dx < 0.5F) x1--;
				if (dy < 0.5F) y1--;
			}
		}
		
		int x2 = x1 + 1;
		int y2 = x1 + 1;
		int z2 = x1 + 1;
		
		dx = (float) (x - x1);
		dy = (float) (y - y1);
		dz = (float) (z - z1);
		
		float l1 = blockView.getBrightness(x1, y1, z1);
		float l2 = blockView.getBrightness(x2, y1, z1);
		float l3 = blockView.getBrightness(x1, y1, z2);
		float l4 = blockView.getBrightness(x2, y1, z2);
		float l5 = blockView.getBrightness(x1, y2, z1);
		float l6 = blockView.getBrightness(x2, y2, z1);
		float l7 = blockView.getBrightness(x1, y2, z2);
		float l8 = blockView.getBrightness(x2, y2, z2);
		
		//LogUtil.log(l1, dx, dy, dz);
		
		l1 = MathUtil.lerp(l1, l2, dx);
		l2 = MathUtil.lerp(l3, l4, dx);
		l3 = MathUtil.lerp(l5, l6, dx);
		l4 = MathUtil.lerp(l7, l8, dx);
		
		l1 = MathUtil.lerp(l1, l2, dz);
		l2 = MathUtil.lerp(l3, l4, dz);
		
		return MathUtil.lerp(l1, l2, dy);*/
	//}
	
	/*@Inject(method = "quadSmooth", at = @At("HEAD"), cancellable = true)
	private void test_quadSmooth(Direction face, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, CallbackInfo info) {
		info.cancel();
		
		int minX1 = MathHelper.floor(x1 - 0.5);
		int minY1 = MathHelper.floor(y1 - 0.5);
		int minZ1 = MathHelper.floor(z1 - 0.5);
		int minX2 = MathHelper.floor(x2 - 0.5);
		int minY2 = MathHelper.floor(y2 - 0.5);
		int minZ2 = MathHelper.floor(z2 - 0.5);
		int minX3 = MathHelper.floor(x3 - 0.5);
		int minY3 = MathHelper.floor(y3 - 0.5);
		int minZ3 = MathHelper.floor(z3 - 0.5);
		int minX4 = MathHelper.floor(x4 - 0.5);
		int minY4 = MathHelper.floor(y4 - 0.5);
		int minZ4 = MathHelper.floor(z4 - 0.5);
		
		int maxX1 = minX1 + 1;
		int maxY1 = minY1 + 1;
		int maxZ1 = minZ1 + 1;
		int maxX2 = minX2 + 1;
		int maxY2 = minY2 + 1;
		int maxZ2 = minZ2 + 1;
		int maxX3 = minX3 + 1;
		int maxY3 = minY3 + 1;
		int maxZ3 = minZ3 + 1;
		int maxX4 = minX4 + 1;
		int maxY4 = minY4 + 1;
		int maxZ4 = minZ4 + 1;
		
		float dx1 = (float) (x1 - minX1);
		float dy1 = (float) (y1 - minY1);
		float dz1 = (float) (z1 - minZ1);
		float dx2 = (float) (x2 - minX2);
		float dy2 = (float) (y2 - minY2);
		float dz2 = (float) (z2 - minZ2);
		float dx3 = (float) (x3 - minX3);
		float dy3 = (float) (y3 - minY3);
		float dz3 = (float) (z3 - minZ3);
		float dx4 = (float) (x4 - minX4);
		float dy4 = (float) (y4 - minY4);
		float dz4 = (float) (z4 - minZ4);
		
		boolean lessThanHalfX, lessThanHalfY, lessThanHalfZ;
		
		switch(face.axis) {
			case X:
				lessThanHalfY = dy1 < 0.5F;
				lessThanHalfZ = dz1 < 0.5F;
				dy1 = lessThanHalfY ? dy1 + 0.5F : dy1 - 0.5F;
				dz1 = lessThanHalfZ ? dz1 + 0.5F : dz1 - 0.5F;
				dy2 = lessThanHalfY ? dy2 + 0.5F : dy2 - 0.5F;
				dz2 = lessThanHalfZ ? dz2 + 0.5F : dz2 - 0.5F;
				dy3 = lessThanHalfY ? dy3 + 0.5F : dy3 - 0.5F;
				dz3 = lessThanHalfZ ? dz3 + 0.5F : dz3 - 0.5F;
				dy4 = lessThanHalfY ? dy4 + 0.5F : dy4 - 0.5F;
				dz4 = lessThanHalfZ ? dz4 + 0.5F : dz4 - 0.5F;
				switch(face.direction) {
					case POSITIVE:
						this.light[0] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx1, dy1, dz1, this.light(minX1, minY1, minZ1), this.light(maxX1, minY1, minZ1), this.light(minX1, maxY1, minZ1), this.light(maxX1, maxY1, minZ1), this.light(minX1, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX1, minY1, minZ1)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX1, maxY1, maxZ1)] ? maxY1 : minY1, maxZ1), this.light(maxX1, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX1, minY1, minZ1)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX1, maxY1, maxZ1)] ? maxY1 : minY1, maxZ1), this.light(minX1, maxY1, maxZ1), this.light(maxX1, maxY1, maxZ1));
						this.light[1] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx2, dy2, dz2, this.light(minX2, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX2, minY2, maxZ2)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX2, maxY2, minZ2)] ? maxY2 : minY2, minZ2), this.light(maxX2, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX2, minY2, maxZ2)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX2, maxY2, minZ2)] ? maxY2 : minY2, minZ2), this.light(minX2, maxY2, minZ2), this.light(maxX2, maxY2, minZ2), this.light(minX2, minY2, maxZ2), this.light(maxX2, minY2, maxZ2), this.light(minX2, maxY2, maxZ2), this.light(maxX2, maxY2, maxZ2));
						this.light[2] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx3, dy3, dz3, this.light(minX3, minY3, minZ3), this.light(maxX3, minY3, minZ3), this.light(minX3, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX3, maxY3, maxZ3)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX3, minY3, minZ3)] ? minY3 : maxY3, minZ3), this.light(maxX3, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX3, maxY3, maxZ3)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX3, minY3, minZ3)] ? minY3 : maxY3, minZ3), this.light(minX3, minY3, maxZ3), this.light(maxX3, minY3, maxZ3), this.light(minX3, maxY3, maxZ3), this.light(maxX3, maxY3, maxZ3));
						this.light[3] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx4, dy4, dz4, this.light(minX4, minY4, minZ4), this.light(maxX4, minY4, minZ4), this.light(minX4, maxY4, minZ4), this.light(maxX4, maxY4, minZ4), this.light(minX4, minY4, maxZ4), this.light(maxX4, minY4, maxZ4), this.light(minX4, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX4, maxY4, minZ4)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX4, minY4, maxZ4)] ? minY4 : maxY4, maxZ4), this.light(maxX4, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX4, maxY4, minZ4)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX4, minY4, maxZ4)] ? minY4 : maxY4, maxZ4));
						return;
					case NEGATIVE:
						--minX1;
						--maxX1;
						--minX2;
						--maxX2;
						--minX3;
						--maxX3;
						--minX4;
						--maxX4;
						this.light[0] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx1, dy1, dz1, this.light(minX1, minY1, minZ1), this.light(maxX1, minY1, minZ1), this.light(minX1, maxY1, minZ1), this.light(maxX1, maxY1, minZ1), this.light(minX1, minY1, maxZ1), this.light(maxX1, minY1, maxZ1), this.light(minX1, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX1, minY1, maxZ1)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX1, maxY1, minZ1)] ? minY1 : maxY1, maxZ1), this.light(maxX1, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX1, minY1, maxZ1)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX1, maxY1, minZ1)] ? minY1 : maxY1, maxZ1));
						this.light[1] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx2, dy2, dz2, this.light(minX2, minY2, minZ2), this.light(maxX2, minY2, minZ2), this.light(minX2, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX2, minY2, minZ2)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX2, maxY2, maxZ2)] ? minY2 : maxY2, minZ2), this.light(maxX2, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX2, minY2, minZ2)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX2, maxY2, maxZ2)] ? minY2 : maxY2, minZ2), this.light(minX2, minY2, maxZ2), this.light(maxX2, minY2, maxZ2), this.light(minX2, maxY2, maxZ2), this.light(maxX2, maxY2, maxZ2));
						this.light[2] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx3, dy3, dz3, this.light(minX3, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX3, maxY3, minZ3)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX3, minY3, maxZ3)] ? maxY3 : minY3, minZ3), this.light(maxX3, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX3, maxY3, minZ3)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX3, minY3, maxZ3)] ? maxY3 : minY3, minZ3), this.light(minX3, maxY3, minZ3), this.light(maxX3, maxY3, minZ3), this.light(minX3, minY3, maxZ3), this.light(maxX3, minY3, maxZ3), this.light(minX3, maxY3, maxZ3), this.light(maxX3, maxY3, maxZ3));
						this.light[3] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx4, dy4, dz4, this.light(minX4, minY4, minZ4), this.light(maxX4, minY4, minZ4), this.light(minX4, maxY4, minZ4), this.light(maxX4, maxY4, minZ4), this.light(minX4, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX4, maxY4, maxZ4)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX4, minY4, minZ4)] ? maxY4 : minY4, maxZ4), this.light(maxX4, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX4, maxY4, maxZ4)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX4, minY4, minZ4)] ? maxY4 : minY4, maxZ4), this.light(minX4, maxY4, maxZ4), this.light(maxX4, maxY4, maxZ4));
						return;
				}
			case Y:
				lessThanHalfX = dx1 < 0.5F;
				lessThanHalfZ = dz1 < 0.5F;
				dx1 = lessThanHalfX ? dx1 + 0.5F : dx1 - 0.5F;
				dz1 = lessThanHalfZ ? dz1 + 0.5F : dz1 - 0.5F;
				dx2 = lessThanHalfX ? dx2 + 0.5F : dx2 - 0.5F;
				dz2 = lessThanHalfZ ? dz2 + 0.5F : dz2 - 0.5F;
				dx3 = lessThanHalfX ? dx3 + 0.5F : dx3 - 0.5F;
				dz3 = lessThanHalfZ ? dz3 + 0.5F : dz3 - 0.5F;
				dx4 = lessThanHalfX ? dx4 + 0.5F : dx4 - 0.5F;
				dz4 = lessThanHalfZ ? dz4 + 0.5F : dz4 - 0.5F;
				switch(face.direction) {
					case POSITIVE:
						this.light[0] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx1, dy1, dz1, this.light(minX1, minY1, minZ1), this.light(maxX1, minY1, minZ1), this.light(minX1, maxY1, minZ1), this.light(maxX1, maxY1, minZ1), this.light(minX1, minY1, maxZ1), this.light(maxX1, minY1, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX1, minY1, maxZ1)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX1, minY1, minZ1)] ? minZ1 : maxZ1), this.light(minX1, maxY1, maxZ1), this.light(maxX1, maxY1, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX1, maxY1, maxZ1)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX1, maxY1, minZ1)] ? minZ1 : maxZ1));
						this.light[1] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx2, dy2, dz2, this.light(minX2, minY2, minZ2), this.light(maxX2, minY2, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX2, minY2, minZ2)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX2, minY2, maxZ2)] ? maxZ2 : minZ2), this.light(minX2, maxY2, minZ2), this.light(maxX2, maxY2, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX2, maxY2, minZ2)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX2, maxY2, maxZ2)] ? maxZ2 : minZ2), this.light(minX2, minY2, maxZ2), this.light(maxX2, minY2, maxZ2), this.light(minX2, maxY2, maxZ2), this.light(maxX2, maxY2, maxZ2));
						this.light[2] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx3, dy3, dz3, this.light(minX3, minY3, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX3, minY3, minZ3)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX3, minY3, maxZ3)] ? maxZ3 : minZ3), this.light(maxX3, minY3, minZ3), this.light(minX3, maxY3, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX3, maxY3, minZ3)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX3, maxY3, maxZ3)] ? maxZ3 : minZ3), this.light(maxX3, maxY3, minZ3), this.light(minX3, minY3, maxZ3), this.light(maxX3, minY3, maxZ3), this.light(minX3, maxY3, maxZ3), this.light(maxX3, maxY3, maxZ3));
						this.light[3] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx4, dy4, dz4, this.light(minX4, minY4, minZ4), this.light(maxX4, minY4, minZ4), this.light(minX4, maxY4, minZ4), this.light(maxX4, maxY4, minZ4), this.light(minX4, minY4, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX4, minY4, maxZ4)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX4, minY4, minZ4)] ? minZ4 : maxZ4), this.light(maxX4, minY4, maxZ4), this.light(minX4, maxY4, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX4, maxY4, maxZ4)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX4, maxY4, minZ4)] ? minZ4 : maxZ4), this.light(maxX4, maxY4, maxZ4));
						return;
					case NEGATIVE:
						--minY1;
						--maxY1;
						--minY2;
						--maxY2;
						--minY3;
						--maxY3;
						--minY4;
						--maxY4;
						this.light[0] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx1, dy1, dz1, this.light(minX1, minY1, minZ1), this.light(maxX1, minY1, minZ1), this.light(minX1, maxY1, minZ1), this.light(maxX1, maxY1, minZ1), this.light(minX1, minY1, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX1, minY1, maxZ1)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX1, minY1, minZ1)] ? minZ1 : maxZ1), this.light(maxX1, minY1, maxZ1), this.light(minX1, maxY1, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX1, maxY1, maxZ1)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX1, maxY1, minZ1)] ? minZ1 : maxZ1), this.light(maxX1, maxY1, maxZ1));
						this.light[1] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx2, dy2, dz2, this.light(minX2, minY2, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX2, minY2, minZ2)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX2, minY2, maxZ2)] ? maxZ2 : minZ2), this.light(maxX2, minY2, minZ2), this.light(minX2, maxY2, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX2, maxY2, minZ2)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX2, maxY2, maxZ2)] ? maxZ2 : minZ2), this.light(maxX2, maxY2, minZ2), this.light(minX2, minY2, maxZ2), this.light(maxX2, minY2, maxZ2), this.light(minX2, maxY2, maxZ2), this.light(maxX2, maxY2, maxZ2));
						this.light[2] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx3, dy3, dz3, this.light(minX3, minY3, minZ3), this.light(maxX3, minY3, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX3, minY3, minZ3)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX3, minY3, maxZ3)] ? maxZ3 : minZ3), this.light(minX3, maxY3, minZ3), this.light(maxX3, maxY3, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX3, maxY3, minZ3)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX3, maxY3, maxZ3)] ? maxZ3 : minZ3), this.light(minX3, minY3, maxZ3), this.light(maxX3, minY3, maxZ3), this.light(minX3, maxY3, maxZ3), this.light(maxX3, maxY3, maxZ3));
						this.light[3] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx4, dy4, dz4, this.light(minX4, minY4, minZ4), this.light(maxX4, minY4, minZ4), this.light(minX4, maxY4, minZ4), this.light(maxX4, maxY4, minZ4), this.light(minX4, minY4, maxZ4), this.light(maxX4, minY4, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX4, minY4, maxZ4)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX4, minY4, minZ4)] ? minZ4 : maxZ4), this.light(minX4, maxY4, maxZ4), this.light(maxX4, maxY4, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX4, maxY4, maxZ4)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX4, maxY4, minZ4)] ? minZ4 : maxZ4));
						return;
				}
			case Z:
				lessThanHalfX = dx1 < 0.5F;
				lessThanHalfY = dy1 < 0.5F;
				dx1 = lessThanHalfX ? dx1 + 0.5F : dx1 - 0.5F;
				dy1 = lessThanHalfY ? dy1 + 0.5F : dy1 - 0.5F;
				dx2 = lessThanHalfX ? dx2 + 0.5F : dx2 - 0.5F;
				dy2 = lessThanHalfY ? dy2 + 0.5F : dy2 - 0.5F;
				dx3 = lessThanHalfX ? dx3 + 0.5F : dx3 - 0.5F;
				dy3 = lessThanHalfY ? dy3 + 0.5F : dy3 - 0.5F;
				dx4 = lessThanHalfX ? dx4 + 0.5F : dx4 - 0.5F;
				dy4 = lessThanHalfY ? dy4 + 0.5F : dy4 - 0.5F;
				switch(face.direction) {
					case POSITIVE:
						this.light[0] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx1, dy1, dz1, this.light(minX1, minY1, minZ1), this.light(maxX1, minY1, minZ1), this.light(minX1, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX1, minY1, minZ1)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX1, maxY1, minZ1)] ? minY1 : maxY1, minZ1), this.light(maxX1, maxY1, minZ1), this.light(minX1, minY1, maxZ1), this.light(maxX1, minY1, maxZ1), this.light(minX1, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX1, minY1, maxZ1)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX1, maxY1, maxZ1)] ? minY1 : maxY1, maxZ1), this.light(maxX1, maxY1, maxZ1));
						this.light[1] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx2, dy2, dz2, this.light(minX2, minY2, minZ2), this.light(maxX2, minY2, minZ2), this.light(minX2, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX2, minY2, minZ2)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX2, maxY2, minZ2)] ? minY2 : maxY2, minZ2), this.light(maxX2, maxY2, minZ2), this.light(minX2, minY2, maxZ2), this.light(maxX2, minY2, maxZ2), this.light(minX2, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX2, minY2, maxZ2)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX2, maxY2, maxZ2)] ? minY2 : maxY2, maxZ2), this.light(maxX2, maxY2, maxZ2));
						this.light[2] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx3, dy3, dz3, this.light(minX3, minY3, minZ3), this.light(maxX3, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX3, maxY3, minZ3)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX3, minY3, minZ3)] ? maxY3 : minY3, minZ3), this.light(minX3, maxY3, minZ3), this.light(maxX3, maxY3, minZ3), this.light(minX3, minY3, maxZ3), this.light(maxX3, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX3, maxY3, maxZ3)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX3, minY3, maxZ3)] ? maxY3 : minY3, maxZ3), this.light(minX3, maxY3, maxZ3), this.light(maxX3, maxY3, maxZ3));
						this.light[3] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx4, dy4, dz4, this.light(minX4, minY4, minZ4), this.light(maxX4, minY4, minZ4), this.light(minX4, maxY4, minZ4), this.light(maxX4, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX4, minY4, minZ4)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX4, maxY4, minZ4)] ? minY4 : maxY4, minZ4), this.light(minX4, minY4, maxZ4), this.light(maxX4, minY4, maxZ4), this.light(minX4, maxY4, maxZ4), this.light(maxX4, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX4, minY4, maxZ4)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX4, maxY4, maxZ4)] ? minY4 : maxY4, maxZ4));
						break;
					case NEGATIVE:
						--minZ1;
						--maxZ1;
						--minZ2;
						--maxZ2;
						--minZ3;
						--maxZ3;
						--minZ4;
						--maxZ4;
						this.light[0] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx1, dy1, dz1, this.light(minX1, minY1, minZ1), this.light(maxX1, minY1, minZ1), this.light(minX1, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX1, minY1, minZ1)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX1, maxY1, minZ1)] ? minY1 : maxY1, minZ1), this.light(maxX1, maxY1, minZ1), this.light(minX1, minY1, maxZ1), this.light(maxX1, minY1, maxZ1), this.light(minX1, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX1, minY1, maxZ1)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX1, maxY1, maxZ1)] ? minY1 : maxY1, maxZ1), this.light(maxX1, maxY1, maxZ1));
						this.light[1] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx2, dy2, dz2, this.light(minX2, minY2, minZ2), this.light(maxX2, minY2, minZ2), this.light(minX2, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX2, minY2, minZ2)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX2, maxY2, minZ2)] ? minY2 : maxY2, minZ2), this.light(maxX2, maxY2, minZ2), this.light(minX2, minY2, maxZ2), this.light(maxX2, minY2, maxZ2), this.light(minX2, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX2, minY2, maxZ2)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX2, maxY2, maxZ2)] ? minY2 : maxY2, maxZ2), this.light(maxX2, maxY2, maxZ2));
						this.light[2] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx3, dy3, dz3, this.light(minX3, minY3, minZ3), this.light(maxX3, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX3, maxY3, minZ3)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX3, minY3, minZ3)] ? maxY3 : minY3, minZ3), this.light(minX3, maxY3, minZ3), this.light(maxX3, maxY3, minZ3), this.light(minX3, minY3, maxZ3), this.light(maxX3, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX3, maxY3, maxZ3)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX3, minY3, maxZ3)] ? maxY3 : minY3, maxZ3), this.light(minX3, maxY3, maxZ3), this.light(maxX3, maxY3, maxZ3));
						this.light[3] = net.modificationstation.stationapi.api.util.math.MathHelper.interpolate3D(dx4, dy4, dz4, this.light(minX4, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX4, maxY4, minZ4)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX4, minY4, minZ4)] ? maxY4 : minY4, minZ4), this.light(maxX4, minY4, minZ4), this.light(minX4, maxY4, minZ4), this.light(maxX4, maxY4, minZ4), this.light(minX4, !BaseBlock.ALLOWS_GRASS_UNDER[this.id(minX4, maxY4, maxZ4)] && !BaseBlock.ALLOWS_GRASS_UNDER[this.id(maxX4, minY4, maxZ4)] ? maxY4 : minY4, maxZ4), this.light(maxX4, minY4, maxZ4), this.light(minX4, maxY4, maxZ4), this.light(maxX4, maxY4, maxZ4));
				}
		}
	}*/
}
