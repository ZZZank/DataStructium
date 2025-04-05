package zank.mods.datastructium.mixin.cache_number_tag;

import net.minecraft.nbt.FloatTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zank.mods.datastructium.utils.CachedTags;

/**
 * @author ZZZank
 */
@Mixin(FloatTag.class)
public class MixinFloatTag {

    @Inject(method = "valueOf", at = @At("HEAD"), cancellable = true)
    private static void replace(float data, CallbackInfoReturnable<FloatTag> cir) {
        cir.setReturnValue(!CachedTags.isFloatInteger(data) || data < CachedTags.START || data >= CachedTags.END
            ? new FloatTag(data)
            : CachedTags.getCachedFloat(data));
    }
}
