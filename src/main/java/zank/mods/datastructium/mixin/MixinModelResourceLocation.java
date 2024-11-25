package zank.mods.datastructium.mixin;

import lombok.val;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zank.mods.datastructium.Pools;
import zank.mods.datastructium.api.ModelResourceLocationAccessor;

import java.util.Arrays;

/**
 * @author ZZZank
 */
@Mixin(ModelResourceLocation.class)
public abstract class MixinModelResourceLocation extends ResourceLocation implements ModelResourceLocationAccessor {

    @Mutable
    @Shadow
    @Final
    private String variant;
    @Unique
    private String[] dataStruct$properties;

    protected MixinModelResourceLocation() {
        super(null, null);
    }

    @Override
    public String[] dataStruct$properties() {
        return this.dataStruct$properties;
    }

    @Inject(method = "<init>([Ljava/lang/String;)V", at = @At("RETURN"))
    private void deduplicate(String[] strings, CallbackInfo ci) {
        val split = this.variant.split(",");
        this.dataStruct$properties = new String[split.length];
        for (int i = 0; i < split.length; i++) {
            this.dataStruct$properties[i] = Pools.MODEL_PROPERTIES.unique(split[i]);
        }
        this.variant = Pools.MODEL_PROPERTIES.unique(variant);
    }

    @Inject(method = "equals", at = @At("HEAD"), cancellable = true)
    private void reEquals(Object object, CallbackInfoReturnable<Boolean> cir) {
        if (this == object) {
            cir.setReturnValue(true);
        } else if (object instanceof ModelResourceLocation) {
            val another = (ModelResourceLocation) object;
            cir.setReturnValue(
                this.namespace.equals(
                    another.getNamespace())
                    && this.path.equals(another.getPath())
                    && Arrays.equals(
                    this.dataStruct$properties,
                    ((ModelResourceLocationAccessor) another).dataStruct$properties()
                )
            );
        } else {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "hashCode", at = @At("HEAD"), cancellable = true, expect = 3)
    private void reHash(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(
            31 * (31 * this.namespace.hashCode() + this.path.hashCode()) + Arrays.hashCode(this.dataStruct$properties)
        );
    }
}