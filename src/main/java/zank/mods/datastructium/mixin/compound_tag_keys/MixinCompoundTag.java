package zank.mods.datastructium.mixin.compound_tag_keys;

import net.minecraft.nbt.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zank.mods.datastructium.mods.minecraft.DataStructiumCompoundTagType;

@Mixin(CompoundTag.class)
public abstract class MixinCompoundTag {

    @Mutable
    @Shadow
    @Final
    public static TagType<CompoundTag> TYPE;

    @Inject(method = "<clinit>", at = @At(value = "RETURN"))
    private static void replaceTagType(CallbackInfo ci) {
        TYPE = new DataStructiumCompoundTagType();
    }
}