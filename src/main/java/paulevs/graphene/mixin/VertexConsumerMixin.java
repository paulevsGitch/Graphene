package paulevs.graphene.mixin;

import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.util.math.Matrix4f;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import net.modificationstation.stationapi.api.util.math.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bhcore.storage.vector.Vec2F;
import paulevs.bhcore.storage.vector.Vec3F;
import paulevs.graphene.storage.QuadData;
import paulevs.graphene.storage.QuadData.VertexInfo;

@Mixin(VertexConsumer.class)
public interface VertexConsumerMixin {
	@Inject(
		method = "quad(Lnet/modificationstation/stationapi/api/util/math/MatrixStack$Entry;Lnet/modificationstation/stationapi/api/client/render/model/BakedQuad;[FFFF[IIZ)V",
		at = @At("HEAD"), cancellable = true
	)
	default void quad(MatrixStack.Entry matrixEntry, BakedQuad quad, float[] brightnesses, float red, float green, float blue, int[] lights, int overlay, boolean useQuadColorData, CallbackInfo info) {
		if (!QuadData.canApply()) return;
		info.cancel();
		
		Matrix4f matrix4f = matrixEntry.getPositionMatrix();
		
		
		for (byte i = 0; i < 4; i++) {
			VertexInfo vertexInfo = QuadData.getInfo(i);
			Vector4f pos = QuadData.getPos4F(i);
			Vec3F normal = vertexInfo.normal;
			Vec3F light = vertexInfo.light;
			Vec2F uv = vertexInfo.uv;
			pos.transform(matrix4f);
			this.vertex(
				pos.getX(), pos.getY(), pos.getZ(),
				light.x, light.y, light.z, 1.0f,
				uv.x, uv.y,
				overlay,
				lights[i],
				normal.x, normal.y, normal.z
			);
		}
		
		/*int[] js = quad.getVertexData();
		Vec3i vec3i = quad.getFace().vector;
		Vec3f vec3f = new Vec3f(vec3i.x, vec3i.y, vec3i.z);
		Matrix4f matrix4f = matrixEntry.getPositionMatrix();
		vec3f.transform(matrixEntry.getNormalMatrix());
		int j = js.length / 8;
		ByteBuffer byteBuffer = BUFFER;
		byteBuffer.clear();
		IntBuffer intBuffer = byteBuffer.asIntBuffer();
		for (int k = 0; k < j; ++k) {
			intBuffer.clear();
			intBuffer.put(js, k * 8, 8);
			float x = byteBuffer.getFloat(0);
			float y = byteBuffer.getFloat(4);
			float z = byteBuffer.getFloat(8);
			float r;
			float g;
			float b;
			if (useQuadColorData) {
				float tmpr = (float) (byteBuffer.get(12) & 0xFF) / 255.0f;
				float tmpg = (float) (byteBuffer.get(13) & 0xFF) / 255.0f;
				float tmpb = (float) (byteBuffer.get(14) & 0xFF) / 255.0f;
				r = tmpr * brightnesses[k] * red;
				g = tmpg * brightnesses[k] * green;
				b = tmpb * brightnesses[k] * blue;
			} else {
				r = brightnesses[k] * red;
				g = brightnesses[k] * green;
				b = brightnesses[k] * blue;
			}
			Vector4f vector4f = new Vector4f(x, y, z, 1.0f);
			vector4f.transform(matrix4f);
			this.vertex(
				vector4f.getX(), vector4f.getY(), vector4f.getZ(),
				r, g, b, 1.0f,
				byteBuffer.getFloat(16), byteBuffer.getFloat(20),
				overlay,
				lights[k],
				vec3f.getX(), vec3f.getY(), vec3f.getZ()
			);
		}*/
	}
	
	@Shadow
	default void vertex(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ) {}
}
