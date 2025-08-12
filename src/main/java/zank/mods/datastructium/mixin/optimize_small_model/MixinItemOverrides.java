package zank.mods.datastructium.mixin.optimize_small_model;

import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zank.mods.datastructium.utils.CollectUtils;

import java.util.List;
import java.util.function.Function;

/**
 * @author ZZZank
 */
@Mixin(ItemOverrides.class)
public class MixinItemOverrides {

    @Mutable
    @Shadow
    @Final
    private List<BakedModel> overrideModels;

    @Mutable
    @Shadow
    @Final
    private List<ItemOverride> overrides;

    @Inject(method = "<init>(Lnet/minecraft/client/resources/model/ModelBakery;Lnet/minecraft/client/resources/model/UnbakedModel;Ljava/util/function/Function;Ljava/util/function/Function;Ljava/util/List;)V", at = @At("RETURN"))
    private void reduceList(
        ModelBakery arg,
        UnbakedModel arg2,
        Function<ResourceLocation, UnbakedModel> function,
        Function<Material, TextureAtlasSprite> textureGetter,
        List<ItemOverride> list,
        CallbackInfo ci
    ) {
        this.overrideModels = CollectUtils.reduceSmallList(this.overrideModels);
        this.overrides = CollectUtils.reduceSmallList(this.overrides);
    }
}
