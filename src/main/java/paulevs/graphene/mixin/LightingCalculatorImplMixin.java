package paulevs.graphene.mixin;

import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.aocalc.LightingCalculatorImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bhcore.util.MathUtil;

@Mixin(value = LightingCalculatorImpl.class, remap = false)
public class LightingCalculatorImplMixin {
	@Shadow private int x, y, z;
	
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
}
