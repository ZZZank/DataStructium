package zank.mods.datastructium.mixin.deduplicate;

import com.mojang.blaze3d.platform.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zank.mods.datastructium.DSConfig;
import zank.mods.datastructium.pools.ShaderCacheLoader;

@Mixin(GlStateManager.class)
public abstract class GLStateManagerMixin {

    @Inject(method = "_glGetUniformLocation", at = @At("RETURN"), cancellable = true)
    private static void getUniform(int u, CharSequence sequence, CallbackInfoReturnable<Integer> cir) {
        if (DSConfig.CACHE_SHADER_UNIFORMS) {
            cir.setReturnValue(ShaderCacheLoader.uniform(u, sequence));
        }
    }
}