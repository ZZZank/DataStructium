package zank.mods.datastructium.mixin.data_struct;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.MultiPartBakedModel;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zank.mods.datastructium.utils.CollectUtils;

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author ZZZank
 */
@Mixin(value = MultiPartBakedModel.class, priority = 345)
public abstract class MixinMultiPartBakedModel {

    @Mutable
    @Shadow
    @Final
    private Map<BlockState, BitSet> selectorCache;

    @Mutable
    @Shadow
    @Final
    private List<Pair<Predicate<BlockState>, BakedModel>> selectors;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void replaceInternal(List<Pair<Predicate<BlockState>, BakedModel>> list, CallbackInfo ci) {
        this.selectorCache = new Reference2ObjectOpenHashMap<>(this.selectorCache);
        this.selectors = CollectUtils.reduceSmallList(this.selectors);
    }
}
