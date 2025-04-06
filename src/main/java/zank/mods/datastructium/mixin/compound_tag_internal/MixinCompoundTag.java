package zank.mods.datastructium.mixin.compound_tag_internal;

import com.google.common.collect.Maps;
import lombok.val;
import net.minecraft.nbt.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zank.mods.datastructium.DSConfig;
import zank.mods.datastructium.utils.InternedTieredMap;
import zank.mods.datastructium.utils.TieredInternalMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZZZank
 */
@Mixin(value = CompoundTag.class, priority = 456)
public abstract class MixinCompoundTag {

    @Shadow @Final private Map<String, Tag> tags;

    @ModifyVariable(method = "<init>(Ljava/util/Map;)V", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
    private static Map<String, Tag> replaceInternal(Map<String, Tag> map) {
        if (map instanceof TieredInternalMap) {
            return map;
        } else if (DSConfig.DEDUPLICATE_COMPOUND_TAG_KEYS) {
            return new InternedTieredMap<>(map);
        }
        return new TieredInternalMap<>(map);
    }

    @Inject(method = "copy()Lnet/minecraft/nbt/CompoundTag;", at = @At("HEAD"), cancellable = true)
    private void fastCopy(CallbackInfoReturnable<CompoundTag> cir) {
        if (this.tags instanceof InternedTieredMap<?>) {
            val toCopy = Maps.transformValues(this.tags, Tag::copy);
            val copied = new InternedTieredMap<Tag>();
            toCopy.forEach(copied::putNoIntern);
            cir.setReturnValue(new CompoundTag(copied));
        }
    }

    @Mixin(targets = "net.minecraft.nbt.CompoundTag$1")
    public static abstract class MixinTagType {

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
}
