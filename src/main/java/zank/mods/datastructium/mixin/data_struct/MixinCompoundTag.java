package zank.mods.datastructium.mixin.data_struct;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

/**
 * @author ZZZank
 */
@Mixin(value = CompoundTag.class, priority = 456)
public class MixinCompoundTag {

    @Mutable
    @Shadow
    @Final
    private Map<String, Tag> tags;

    @Inject(method = "<init>(Ljava/util/Map;)V", at = @At("RETURN"))
    private void replaceInternal(Map<String, Tag> tags, CallbackInfo ci) {
        this.tags = tags instanceof Object2ObjectMap ? tags : new Object2ObjectOpenHashMap<>(tags);
    }
}
