package zank.mods.datastructium.mixin.data_struct;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import zank.mods.datastructium.DSConfig;
import zank.mods.datastructium.utils.TieredInternalMap;

import java.util.Map;

/**
 * @author ZZZank
 */
@Mixin(value = CompoundTag.class, priority = 456)
public abstract class MixinCompoundTag {

    @ModifyArg(method = "<init>()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;<init>(Ljava/util/Map;)V"))
    private static Map<String, Tag> replaceInternal(Map<String, Tag> map) {
        if (DSConfig.TIERED_COMPOUND_TAG_INTERNAL) {
            return new TieredInternalMap<>();
        }
        return map;
    }
}
