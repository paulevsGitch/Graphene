package paulevs.graphene.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.sortme.GameRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bhcore.rendering.shaders.ShaderProgram;
import paulevs.bhcore.rendering.shaders.buffers.FrameBuffer;
import paulevs.graphene.Graphene;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Shadow private Minecraft minecraft;
	@Unique private boolean renderShadows;
	
	/*@Inject(method = "delta(FJ)V", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/texture/TextureManager;getTextureId(Ljava/lang/String;)I"
	), cancellable = true)
	public void delta(float l, long par2, CallbackInfo info) {
		System.out.println("Cancel Render!");
		info.cancel();
	}*/
	
	@Inject(method = "delta(FJ)V", at = @At("HEAD"))
	public void graphene_deltaHead(float f, long l, CallbackInfo info) {
		//renderShadows = !renderShadows;
		//if (!renderShadows) return;
		
		//System.out.println("Shadows!");
		
		//System.out.println("Render Start");
		Graphene.shadows.shadowBuffer.bind();
		GL11.glViewport(0, 0, 256, 256);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		//GL11.glClearDepth(0.8F);
		Graphene.shadows.shaderProgram.bindWithUniforms();
		
		//WorldRenderer worldRenderer = this.minecraft.worldRenderer;
		//worldRenderer.renderSky(f);
		//Living living = this.minecraft.viewEntity;
		//worldRenderer.method_1548(living, 0, f);
		
		//delta(f, l);
		
		ShaderProgram.unbind();
		FrameBuffer.unbind();
	}
	
	@Shadow
	public void delta(float f, long l) {}
	
	/*@Inject(method = "delta(FJ)V", at = @At("RETURN"))
	public void graphene_deltaTail(float l, long par2, CallbackInfo info) {
		//System.out.println("Render Stop");
		ShaderProgram.unbind();
		FrameBuffer.unbind();
	}*/
}
