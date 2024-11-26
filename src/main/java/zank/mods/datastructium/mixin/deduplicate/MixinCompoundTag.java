package zank.mods.datastructium.mixin.deduplicate;

import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import zank.mods.datastructium.pools.Pools;

/**
 * @author ZZZank
 */
@Mixin(value = CompoundTag.class, priority = 345)
public abstract class MixinCompoundTag {

    @ModifyArg(
        method = {"put", "putByte", "putShort", "putInt", "putLong", "putUUID", "putFloat",
            "putDouble", "putString", "putByteArray", "putIntArray*", "putLongArray*", "putBoolean"},
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
        ),
        index = 0
    )
    private Object deduplicateKeyOnPut(Object key) {
        return Pools.TAG_KEYS.unique((String) key);
    }
}
