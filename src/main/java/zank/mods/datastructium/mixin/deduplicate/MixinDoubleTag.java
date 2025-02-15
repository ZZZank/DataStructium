package zank.mods.datastructium.mixin.deduplicate;

import net.minecraft.nbt.DoubleTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zank.mods.datastructium.DSConfig;
import zank.mods.datastructium.utils.CachedTags;

/**
 * @author ZZZank
 */
@Mixin(DoubleTag.class)
public class MixinDoubleTag {

    @Inject(method = "valueOf", at = @At("HEAD"), cancellable = true)
    private static void replace(double data, CallbackInfoReturnable<DoubleTag> cir) {
        if (DSConfig.CACHE_NUMBER_TAG) {
            cir.setReturnValue(!CachedTags.isDoubleInteger(data) || data < CachedTags.START || data >= CachedTags.END
                ? new DoubleTag(data)
                : CachedTags.getCachedDouble(data));
        }
    }
}
