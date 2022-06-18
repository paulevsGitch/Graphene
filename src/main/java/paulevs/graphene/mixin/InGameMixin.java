package paulevs.graphene.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.InGame;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bhcore.rendering.shaders.ShaderProgram;
import paulevs.graphene.Graphene;
import paulevs.graphene.storage.Shadows;

@Mixin(InGame.class)
public abstract class InGameMixin {
	@Unique private Shadows shadows;
	
	@Inject(method = "<init>(Lnet/minecraft/client/Minecraft;)V", at = @At("TAIL"))
	private void test_onInit(Minecraft minecraft, CallbackInfo info) {
		Graphene.initClient();
	}
	
	@Inject(method = "renderHud", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V", shift = Shift.AFTER))
	private void test_renderForeground(float f, boolean flag, int i, int j, CallbackInfo info) {
		//Shadows.SHADOWS_PROGRAM.bind();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glColor3f(1, 1, 1);
		
		Graphene.shadows.shadowBuffer.getDepth().bind();
		renderSquare(0, 0, 64, 64);
		//ShaderProgram.unbind();
	}
	
	@Unique
	private void renderSquare(int x, int y, int width, int height) {
		Tessellator tessellator = Tessellator.INSTANCE;
		tessellator.start();
		tessellator.vertex(x, y + height, 0, 0, 1);
		tessellator.vertex(x + width, y + height, 0, 1, 1);
		tessellator.vertex(x + width, y, 0, 1, 0);
		tessellator.vertex(x, y, 0, 0, 0);
		tessellator.draw();
	}
}
