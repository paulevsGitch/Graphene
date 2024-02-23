package paulevs.graphene.mixin.client;

import net.modificationstation.stationapi.api.client.render.model.SpriteAtlasManager.AtlasPreparation;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.api.client.texture.SpriteLoader.StitchResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.graphene.rendering.PropertyAtlas;

@Mixin(value = AtlasPreparation.class, remap = false)
public class AtlasPreparationMixin {
	@Shadow @Final private StitchResult stitchResult;
	
	@Shadow @Final private SpriteAtlasTexture atlasTexture;
	
	@Inject(method = "upload", at = @At("TAIL"))
	private void graphene_upload(CallbackInfo info) {
		PropertyAtlas.init(atlasTexture.getWidth(), atlasTexture.getHeight());
		stitchResult.regions().forEach(PropertyAtlas::addSprite);
		PropertyAtlas.upload();
	}
}
