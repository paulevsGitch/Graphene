package paulevs.graphene.mixin;

import net.modificationstation.stationapi.impl.client.arsenic.renderer.mesh.MutableQuadViewImpl;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.AbstractQuadRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AbstractQuadRenderer.class, remap = false)
public abstract class AbstractQuadRendererMixin {
	@Inject(method = "tessellateSmooth", at = @At("HEAD"), cancellable = true)
	private void graphene_tessellateSmooth(MutableQuadViewImpl q, int blockColorIndex, CallbackInfo info) {
		info.cancel();
		this.colorizeQuad(q, blockColorIndex);
		for (int i = 0; i < 4; i++) {
			q.spriteColour(i, 0, 0xFF0000);
		}
		bufferQuad(q);
	}
	
	@Inject(method = "tessellateFlat", at = @At("HEAD"), cancellable = true)
	private void graphene_tessellateFlat(MutableQuadViewImpl q, int blockColorIndex, CallbackInfo info) {
		info.cancel();
		this.colorizeQuad(q, blockColorIndex);
		for (int i = 0; i < 4; i++) {
			q.spriteColour(i, 0, 0xFF0000);
		}
		bufferQuad(q);
	}
	
	@Shadow protected abstract void bufferQuad(MutableQuadViewImpl q);
	
	@Shadow protected abstract void colorizeQuad(MutableQuadViewImpl q, int blockColourIndex);
}
