package zank.mods.datastructium.mixin.data_struct;

import lombok.val;
import net.minecraft.nbt.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zank.mods.datastructium.DSConfig;
import zank.mods.datastructium.utils.TieredInternalMap;

import java.io.DataInput;
import java.io.IOException;
import java.util.Map;

/**
 * @author ZZZank
 */
@Mixin(value = CompoundTag.class, priority = 456)
public abstract class MixinCompoundTag {

    @ModifyVariable(method = "<init>(Ljava/util/Map;)V", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
    private static Map<String, Tag> replaceInternal(Map<String, Tag> map) {
        if (DSConfig.TIERED_COMPOUND_TAG_INTERNAL && !(map instanceof TieredInternalMap)) {
            return new TieredInternalMap<>(map);
        }
        return map;
    }

    @Mixin(targets = "net.minecraft.nbt.CompoundTag$1")
    public static abstract class MixinTagType {

        @ModifyVariable(method = "load*", at = @At("STORE"))
        private Map<String, Tag> replaceMap(Map<String, Tag> map) {
            if (DSConfig.TIERED_COMPOUND_TAG_INTERNAL) {
                return new TieredInternalMap<>();
            }
            return map;
        }
    }
}
