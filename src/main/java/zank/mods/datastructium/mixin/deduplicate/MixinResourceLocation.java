package zank.mods.datastructium.mixin.deduplicate;

import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zank.mods.datastructium.Pools;

/**
 * @author ZZZank
 */
@Mixin(value = ResourceLocation.class, priority = 345)
public class MixinResourceLocation {

    @Mutable
    @Shadow
    @Final
    protected String namespace;
    @Mutable
    @Shadow
    @Final
    protected String path;

    @Inject(method = "<init>([Ljava/lang/String;)V", at = @At("RETURN"))
    private void replaceKeys(String[] id, CallbackInfo ci) {
        this.namespace = Pools.NAMESPACES.unique(this.namespace);
        this.path = Pools.RL_PATHS.unique(this.path);
    }
}
