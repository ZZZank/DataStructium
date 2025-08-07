package zank.mods.datastructium.mixin.optimize_small_model;

import lombok.val;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import zank.mods.datastructium.utils.CollectUtils;

import java.util.List;
import java.util.Map;

/**
 * @author ZZZank
 */
@Mixin(SimpleBakedModel.class)
public abstract class MixinSimpleBakedModel {

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static List<BakedQuad> reduceUnculledFaces(List<BakedQuad> unculledFaces) {
        return CollectUtils.reduceSmallList(unculledFaces);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static Map<Direction, List<BakedQuad>> reduceCulledFaces(Map<Direction, List<BakedQuad>> culledFaces) {
        for (val entry : culledFaces.entrySet()) {
            entry.setValue(CollectUtils.reduceSmallList(entry.getValue()));
        }
        return culledFaces;
    }
}
