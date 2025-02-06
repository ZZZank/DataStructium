package zank.mods.datastructium.mixin.deduplicate;

import net.minecraft.nbt.IntTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zank.mods.datastructium.DSConfig;
import zank.mods.datastructium.utils.CachedTags;

/**
 * @author ZZZank
 */
@Mixin(IntTag.class)
public abstract class MixinIntTag {

    @Inject(method = "valueOf", at = @At("HEAD"), cancellable = true)
    private static void replace(int data, CallbackInfoReturnable<IntTag> cir) {
        if (DSConfig.ENABLE_NUMBER_TAG_CACHE) {
            cir.setReturnValue(CachedTags.ofInt(data));
        }
    }
}
