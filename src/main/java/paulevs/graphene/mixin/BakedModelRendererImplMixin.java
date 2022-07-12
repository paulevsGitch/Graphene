package paulevs.graphene.mixin;

import net.minecraft.level.BlockView;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.colour.block.BlockColours;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.BakedModelRendererImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bhcore.storage.vector.Vec3F;
import paulevs.graphene.storage.QuadData;
import paulevs.graphene.storage.QuadData.VertexInfo;

import java.util.Random;

@Mixin(value = BakedModelRendererImpl.class, remap = false)
public class BakedModelRendererImplMixin {
	@Shadow @Final private BlockColours blockColours;
	@Unique Random random = new Random(0);
	
	@Inject(method = "renderQuad", at = @At("HEAD"), cancellable = true)
	private void test(BlockView world, BlockState state, BlockPos pos, VertexConsumer vertexConsumer, MatrixStack.Entry matrixEntry, BakedQuad quad, float[] brightness, int overlay, CallbackInfo info) {
		info.cancel();
		
		float b = 0;
		float g = 0;
		float r = 0;
		/*if (quad.hasColour()) {
			int i = blockColours.getColour(state, world, pos, quad.getColorIndex());
			r = (float)(i >> 16 & 0xFF) / 255.0f;
			g = (float)(i >> 8 & 0xFF) / 255.0f;
			b = (float)(i & 0xFF) / 255.0f;
		}
		else {
			r = 1.0f;
			g = 1.0f;
			b = 1.0f;
		}*/
		
		//int hash = quad.hashCode();
		//r = (pos.x & 3) / 3.0F;//(hash >> 16 & 0xFF) / 255.0F;
		//g = (pos.y & 3) / 3.0F;//(hash >> 8 & 0xFF) / 255.0F;
		//b = (pos.z & 3) / 3.0F;//(hash & 0xFF) / 255.0F;
		
		QuadData.fillData(quad.getVertexData(), quad.getFace());
		for (byte i = 0; i < 4; i++) {
			VertexInfo quadInfo = QuadData.getInfo(i);
			//Vec3F position = quadInfo.position;
			Vec3F light = quadInfo.light;
			light.x = random.nextFloat();//MathUtil.clamp(position.x, 0.0F, 1.0F);
			light.y = random.nextFloat();//MathUtil.clamp(position.y, 0.0F, 1.0F);
			light.z = random.nextFloat();//MathUtil.clamp(position.z, 0.0F, 1.0F);
		}
		
		vertexConsumer.quad(matrixEntry, quad, brightness, r, g, b, new int[] { 0, 0, 0, 0 }, overlay, true);
	}
}
