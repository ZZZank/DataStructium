package zank.mods.datastructium.mixin.deduplicate;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zank.mods.datastructium.DSConfig;
import zank.mods.datastructium.pools.Pools;

/**
 * @author ZZZank
 */
@Mixin(value = BakedQuad.class, priority = 345)
public class MixinBakedQuads {

    @Mutable
    @Shadow
    @Final
    protected int[] vertices;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void deduplicate(
        int[] v,
        int i,
        Direction arg,
        TextureAtlasSprite arg2,
        boolean bl,
        CallbackInfo ci
    ) {
        if (DSConfig.dedupQuads) {
            this.vertices = Pools.QUADS.unique(this.vertices);
        }
    }
}
