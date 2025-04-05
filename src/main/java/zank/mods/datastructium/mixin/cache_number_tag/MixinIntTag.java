package zank.mods.datastructium.mixin.cache_number_tag;

import net.minecraft.nbt.IntTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zank.mods.datastructium.utils.CachedTags;

/**
 * @author ZZZank
 */
@Mixin(IntTag.class)
public abstract class MixinIntTag {

    @Inject(method = "valueOf", at = @At("HEAD"), cancellable = true)
    private static void replace(int data, CallbackInfoReturnable<IntTag> cir) {
        cir.setReturnValue(data < CachedTags.START || data >= CachedTags.END
            ? new IntTag(data)
            : CachedTags.getCachedInt(data));
    }
}
