package paulevs.graphene.mixin;

import net.minecraft.client.render.RenderList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderList.class)
public class RenderListMixin {
	@Inject(method = "method_1909()V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", shift = Shift.AFTER))
	private void colorizer_startShaderProgram(CallbackInfo info) {
		System.out.println("StartShaders!");
	}
	
	@Inject(method = "method_1909()V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPopMatrix()V", shift = Shift.BEFORE))
	private void colorizer_endShaderProgram(CallbackInfo info) {
		System.out.println("EndShaders!");
	}
}
