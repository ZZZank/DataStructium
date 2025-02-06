package zank.mods.datastructium.mixin.deduplicate;

import net.minecraft.nbt.FloatTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zank.mods.datastructium.DSConfig;
import zank.mods.datastructium.utils.CachedTags;

/**
 * @author ZZZank
 */
@Mixin(FloatTag.class)
public class MixinFloatTag {

    @Inject(method = "valueOf", at = @At("HEAD"), cancellable = true)
    private static void replace(float data, CallbackInfoReturnable<FloatTag> cir) {
        if (DSConfig.ENABLE_NUMBER_TAG_CACHE) {
            cir.setReturnValue(CachedTags.ofFloat(data));
        }
    }
}
