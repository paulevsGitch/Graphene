package paulevs.graphene.mixin.client;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.graphene.rendering.disposing.DisposeUtil;
import paulevs.graphene.rendering.shaders.Programs;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Inject(method = "init", at = @At("TAIL"))
	private void graphene_onInit(CallbackInfo info) {
		Programs.init();
	}
	
	@Inject(method = "scheduleStop", at = @At("TAIL"))
	private void graphene_onExit(CallbackInfo info) {
		DisposeUtil.disposeAll();
	}
}
