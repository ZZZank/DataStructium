package zank.mods.datastructium.mixin.mods.oculus;

import net.coderbot.iris.pipeline.PipelineManager;
import net.coderbot.iris.pipeline.WorldRenderingPipeline;
import net.coderbot.iris.shaderpack.materialmap.NamespacedId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zank.mods.datastructium.pools.ShaderCacheLoader;

/**
 * @author ZZZank
 */

@Mixin(value = PipelineManager.class, remap = false)
public abstract class MixinPipelineManager {

    @Inject(
        method = "preparePipeline",
        at = @At(
            value = "INVOKE",
            target = "Lnet/coderbot/iris/IrisLogging;info(Ljava/lang/String;[Ljava/lang/Object;)V"
        )
    )
    private void reloadCacheOnCreatedPipeline(
        NamespacedId currentDimension,
        CallbackInfoReturnable<WorldRenderingPipeline> cir
    ) {
        ShaderCacheLoader.reload("new Oculus pipeline");
    }
}