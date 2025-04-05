package zank.mods.datastructium.mixin.compound_tag_internal;

import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import zank.mods.datastructium.DSConfig;
import zank.mods.datastructium.utils.InternedTieredMap;
import zank.mods.datastructium.utils.TieredInternalMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZZZank
 */
@Mixin(targets = "net/minecraft/nbt/CompoundTag$1")
public abstract class MixinCompoundTag1 {

    @Redirect(
        method = "load(Ljava/io/DataInput;ILnet/minecraft/nbt/NbtAccounter;)Lnet/minecraft/nbt/CompoundTag;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/Maps;newHashMap()Ljava/util/HashMap;"
        )
    )
    private HashMap<?, ?> removeAllocation() {
        return null;
    }

    @ModifyVariable(
        method = "load(Ljava/io/DataInput;ILnet/minecraft/nbt/NbtAccounter;)Lnet/minecraft/nbt/CompoundTag;",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lcom/google/common/collect/Maps;newHashMap()Ljava/util/HashMap;"
        )
    )
    private Map<String, Tag> replaceMap(Map<String, Tag> map) {
        if (DSConfig.DEDUPLICATE_COMPOUND_TAG_KEYS) {
            return new InternedTieredMap<>(map);
        }
        return new TieredInternalMap<>(map);
    }
}
