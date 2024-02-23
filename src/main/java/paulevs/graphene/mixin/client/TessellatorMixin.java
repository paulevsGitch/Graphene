package paulevs.graphene.mixin.client;

import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.graphene.rendering.TessellatorUVFixer;

@Mixin(Tessellator.class)
public class TessellatorMixin implements TessellatorUVFixer {
	@Unique private static final float[] GRAPHENE_UV = new float[12];
	@Unique private static boolean graphene_rendering = false;
	
	@Shadow private int vertexAmount;
	@Shadow private int[] bufferArray;
	@Shadow private static boolean useTriangles;
	@Shadow private int index;
	
	@Override
	public void graphene_setRendering(boolean rendering) {
		graphene_rendering = rendering;
	}
	
	@Inject(method = "addVertex", at = @At(
		value = "FIELD",
		target = "Lnet/minecraft/client/render/Tessellator;bufferArray:[I",
		ordinal = 18,
		shift = Shift.AFTER
	))
	private void graphene_addVertex(double x, double y, double z, CallbackInfo info) {
		if (!graphene_rendering) return;
		if ((vertexAmount & 3) != 0) return;
		
		byte count = useTriangles ? (byte) 6 : (byte) 4;
		float au = 0.0F;
		float av = 0.0F;
		
		for (byte index = 0; index < count; index++) {
			int uvIndex = index << 1;
			int dataIndex = this.index - index * 8;
			float u = Float.intBitsToFloat(bufferArray[dataIndex + 3]);
			float v = Float.intBitsToFloat(bufferArray[dataIndex + 4]);
			GRAPHENE_UV[uvIndex] = u;
			GRAPHENE_UV[uvIndex | 1] = v;
			au += u;
			av += v;
		}
		
		au /= count;
		av /= count;
		
		for (byte index = 0; index < count; index++) {
			int uvIndex = index << 1;
			int dataIndex = this.index - index * 8;
			float u = MathHelper.lerp(0.001F, GRAPHENE_UV[uvIndex], au);
			float v = MathHelper.lerp(0.001F, GRAPHENE_UV[uvIndex | 1], av);
			bufferArray[dataIndex + 3] = Float.floatToRawIntBits(u);
			bufferArray[dataIndex + 4] = Float.floatToRawIntBits(v);
		}
	}
}
