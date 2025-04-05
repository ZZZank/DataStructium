package zank.mods.datastructium.mixin.canonicalize_quads;

import net.minecraft.client.renderer.block.model.BakedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import zank.mods.datastructium.pools.Pools;

/**
 * @author ZZZank
 */
@Mixin(value = BakedQuad.class, priority = 345)
public abstract class MixinBakedQuads {

    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private static int[] deduplicateQuads(int[] vertices) {
        return Pools.QUADS.unique(vertices);
    }
}
