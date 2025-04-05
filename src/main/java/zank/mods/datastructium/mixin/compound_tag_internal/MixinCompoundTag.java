package zank.mods.datastructium.mixin.compound_tag_internal;

import net.minecraft.nbt.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import zank.mods.datastructium.DSConfig;
import zank.mods.datastructium.utils.InternedTieredMap;
import zank.mods.datastructium.utils.TieredInternalMap;

import java.util.Map;

/**
 * @author ZZZank
 */
@Mixin(value = CompoundTag.class, priority = 456)
public abstract class MixinCompoundTag {

    @ModifyVariable(method = "<init>(Ljava/util/Map;)V", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
    private static Map<String, Tag> replaceInternal(Map<String, Tag> map) {
        if (map instanceof TieredInternalMap) {
            return map;
        } else if (DSConfig.DEDUPLICATE_COMPOUND_TAG_KEYS) {
            return new InternedTieredMap<>(map);
        }
        return new TieredInternalMap<>(map);
    }
}
