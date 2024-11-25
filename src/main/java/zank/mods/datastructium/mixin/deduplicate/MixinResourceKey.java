package zank.mods.datastructium.mixin.deduplicate;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zank.mods.datastructium.Pools;

import java.util.Objects;

/**
 * @author ZZZank
 */
@Mixin(value = ResourceKey.class, priority = 345)
public class MixinResourceKey {

    @Mutable
    @Shadow
    @Final
    private ResourceLocation registryName;
    @Mutable
    @Shadow
    @Final
    private ResourceLocation location;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void deduplicate(ResourceLocation registry, ResourceLocation location, CallbackInfo ci) {
        this.registryName = Pools.REGISTRY_KEYS.unique(this.registryName);
        this.location = Pools.REGISTRY_LOCATIONS.unique(this.location);
    }

    @Override
    public int hashCode() {
        return 31 * registryName.hashCode() + location.hashCode();
    }
}
