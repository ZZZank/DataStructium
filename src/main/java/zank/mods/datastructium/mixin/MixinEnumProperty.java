package zank.mods.datastructium.mixin;

import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author ZZZank
 */
@Mixin(EnumProperty.class)
public class MixinEnumProperty {

    @Unique
    private Integer dataStruct$cachedHash = null;

    @Inject(method = "generateHashCode", at = @At("HEAD"), cancellable = true)
    public void replaceHash(CallbackInfoReturnable<Integer> cir) {
        if (dataStruct$cachedHash != null) {
            cir.setReturnValue(dataStruct$cachedHash);
        }
    }

    @Inject(method = "generateHashCode", at = @At("RETURN"))
    public void captureHash(CallbackInfoReturnable<Integer> cir) {
        dataStruct$cachedHash = cir.getReturnValue();
    }
}
