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

    @Shadow
    private static byte readNamedTagType(DataInput input, NbtAccounter accounter) throws IOException {
        throw new IllegalStateException("calling mixin shadow method");
    }

    @Shadow
    private static String readNamedTagName(DataInput input, NbtAccounter accounter) throws IOException {
        throw new IllegalStateException("calling mixin shadow method");
    }

    @Shadow
    private static Tag readNamedTagData(
        TagType<?> type,
        String name,
        DataInput input,
        int depth,
        NbtAccounter accounter
    ) {
        throw new IllegalStateException("calling mixin shadow method");
    }

    @Mixin(targets = "net.minecraft.nbt.CompoundTag$1")
    public static abstract class MixinTagType {

        @Inject(
            method = "load(Ljava/io/DataInput;ILnet/minecraft/nbt/NbtAccounter;)Lnet/minecraft/nbt/CompoundTag;",
            at = @At(
                value = "INVOKE",
                target = "Lcom/google/common/collect/Maps;newHashMap()Ljava/util/HashMap;"
            ),
            cancellable = true
        )
        private void replaceAllbody(
            DataInput input,
            int depth,
            NbtAccounter accounter,
            CallbackInfoReturnable<CompoundTag> cir
        ) throws IOException {
            if (!DSConfig.TIERED_COMPOUND_TAG_INTERNAL) {
                return;
            }
            val map = new TieredInternalMap<String, Tag>();

            byte type;
            while ((type = readNamedTagType(input, accounter)) != 0) {
                val name = readNamedTagName(input, accounter);
                accounter.accountBits(224 + name.length() * 16L);
                accounter.accountBits(32L);

                val data = readNamedTagData(TagTypes.getType(type), name, input, depth + 1, accounter);
                if (map.put(name, data) != null) {
                    accounter.accountBits(288L);
                }
            }

            cir.setReturnValue(new CompoundTag(map));
        }
    }
}
