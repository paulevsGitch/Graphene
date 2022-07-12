package paulevs.graphene.mixin;

import net.minecraft.block.BaseBlock;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.aocalc.LightingCalculatorImpl;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.mesh.MutableQuadViewImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(value = LightingCalculatorImpl.class, remap = false)
public class LightingCalculatorImplMixin {
	@Shadow @Final public float[] light;
	
	@Inject(method = "initialize", at = @At("HEAD"))
	private void graphene_onAOInit(BaseBlock block, BlockView blockView, int x, int y, int z, boolean ao, CallbackInfo info) {
		Arrays.fill(this.light, 1.0F);
	}
	
	@Inject(
		method = "calculateForQuad(Lnet/modificationstation/stationapi/api/client/render/model/BakedQuad;)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void graphene_disableQuadLight(BakedQuad q, CallbackInfo info) {
		info.cancel();
	}
	
	@Inject(
		method = "calculateForQuad(Lnet/modificationstation/stationapi/impl/client/arsenic/renderer/mesh/MutableQuadViewImpl;)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void graphene_disableQuadLight(MutableQuadViewImpl q, CallbackInfo info) {
		info.cancel();
	}
}
