package paulevs.graphene.mixin.client;

import net.minecraft.client.render.GameRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import paulevs.graphene.rendering.shaders.Programs;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Shadow float fogColorR;
	@Shadow float fogColorG;
	@Shadow float fogColorB;
	
	@ModifyArgs(method = "setupFog", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glFogi(II)V"))
	private void graphene_setFogType(Args args) {
		if ((int) args.get(0) != GL11.GL_FOG_MODE) return;
		if ((int) args.get(1) == GL11.GL_EXP) {
			Programs.TERRAIN_FOG_PARAMS.setY(0.0F);
		}
	}
	
	@ModifyArgs(method = "setupFog", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glFogf(IF)V"))
	private void graphene_setFogParam(Args args) {
		int param = args.get(0);
		float value = args.get(1);
		switch (param) {
			case GL11.GL_FOG_DENSITY, GL11.GL_FOG_START -> Programs.TERRAIN_FOG_PARAMS.setX(value);
			case GL11.GL_FOG_END -> Programs.TERRAIN_FOG_PARAMS.setY(value);
		}
	}
	
	@Inject(method = "setupFog", at = @At("TAIL"))
	private void setColor(int type, float delta, CallbackInfo info) {
		Programs.TERRAIN_FOG_COLOR.setValue(fogColorR, fogColorG, fogColorB);
	}
}
