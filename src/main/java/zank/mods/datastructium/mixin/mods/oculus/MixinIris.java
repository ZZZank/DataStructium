package zank.mods.datastructium.mixin.mods.oculus;

import net.coderbot.iris.Iris;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zank.mods.datastructium.pools.ShaderCacheLoader;

/**
 * @author ZZZank
 */
@Mixin(value = Iris.class, remap = false)
public class MixinIris {

    @Inject(method = "reload", at = @At("HEAD"))
    private static void reloadCache(CallbackInfo ci) {
        ShaderCacheLoader.reload("Oculus Shader Reload");
    }
}
