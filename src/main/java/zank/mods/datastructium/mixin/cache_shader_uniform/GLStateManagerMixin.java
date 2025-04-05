package zank.mods.datastructium.mixin.cache_shader_uniform;

import com.mojang.blaze3d.platform.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import zank.mods.datastructium.pools.ShaderCacheLoader;

@Mixin(GlStateManager.class)
public abstract class GLStateManagerMixin {

    @Redirect(
        method = "_glGetUniformLocation",
        at = @At(
            value = "INVOKE",
            target = "Lorg/lwjgl/opengl/GL20;glGetUniformLocation(ILjava/lang/CharSequence;)I"
        )
    )
    private static int getUniform(int program, CharSequence name) {
        return ShaderCacheLoader.uniform(program, name);
    }
}