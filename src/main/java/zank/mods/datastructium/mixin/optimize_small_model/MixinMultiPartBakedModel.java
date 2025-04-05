package zank.mods.datastructium.mixin.optimize_small_model;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.MultiPartBakedModel;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import zank.mods.datastructium.utils.CollectUtils;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author ZZZank
 */
@Mixin(value = MultiPartBakedModel.class, priority = 345)
public abstract class MixinMultiPartBakedModel {

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static List<Pair<Predicate<BlockState>, BakedModel>> shrinkList(List<Pair<Predicate<BlockState>, BakedModel>> list) {
        return CollectUtils.reduceSmallList(list);
    }
}
