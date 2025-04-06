package zank.mods.datastructium.mixin.compound_tag_internal;

import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import zank.mods.datastructium.DSConfig;
import zank.mods.datastructium.utils.InternedTieredMap;
import zank.mods.datastructium.utils.TieredInternalMap;

import java.util.Map;

@Mixin(targets = "net.minecraft.nbt.CompoundNBT$1", remap = false)
public abstract class MixinCompoundTag1 {

    @ModifyVariable(
        method = "func_225649_b_*",
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