package zank.mods.datastructium.mixin.data_struct;

import lombok.val;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zank.mods.datastructium.DSConfig;
import zank.mods.datastructium.utils.CollectUtils;

import java.util.List;
import java.util.Map;

/**
 * @author ZZZank
 */
@Mixin(SimpleBakedModel.class)
public abstract class MixinSimpleBakedModel {

    @Mutable
    @Shadow
    @Final
    protected List<BakedQuad> unculledFaces;
    @Shadow
    @Final
    protected Map<Direction, List<BakedQuad>> culledFaces;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void replaceInternal(
        List<BakedQuad> list,
        Map<Direction, List<BakedQuad>> map,
        boolean bl,
        boolean bl2,
        boolean bl3,
        TextureAtlasSprite arg,
        ItemTransforms arg2,
        ItemOverrides arg3,
        CallbackInfo ci
    ) {
        if (DSConfig.OPTIMIZE_SIMPLE_MODEL) {
            this.unculledFaces = CollectUtils.reduceSmallList(this.unculledFaces);
            for (val entry : this.culledFaces.entrySet()) {
                entry.setValue(CollectUtils.reduceSmallList(entry.getValue()));
            }
        }
    }
}
