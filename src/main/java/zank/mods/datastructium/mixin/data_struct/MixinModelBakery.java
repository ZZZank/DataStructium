package zank.mods.datastructium.mixin.data_struct;

import com.mojang.math.Transformation;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.commons.lang3.tuple.Triple;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Set;

@Mixin(ModelBakery.class)
public abstract class MixinModelBakery {

    @Mutable
    @Shadow
    @Final
    private Map<ResourceLocation, UnbakedModel> unbakedCache;
    @Mutable
    @Shadow
    @Final
    private Map<Triple<ResourceLocation, Transformation, Boolean>, BakedModel> bakedCache;
    @Mutable
    @Shadow
    @Final
    private Map<ResourceLocation, UnbakedModel> topLevelModels;
    @Mutable
    @Shadow
    @Final
    private Map<ResourceLocation, BakedModel> bakedTopLevelModels;
    @Mutable
    @Shadow
    @Final
    private Set<ResourceLocation> loadingStack;

    @Inject(
        method = "<init>(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/client/color/block/BlockColors;Z)V",
        at = @At("RETURN")
    )
    private void replaceInternal(ResourceManager arg, BlockColors arg2, boolean vanillaBakery, CallbackInfo ci) {
        this.unbakedCache = new Object2ObjectOpenHashMap<>(this.unbakedCache);
        this.bakedTopLevelModels = new Object2ObjectOpenHashMap<>(this.bakedTopLevelModels);
        this.bakedCache = new Object2ObjectOpenHashMap<>(this.bakedCache);
        this.topLevelModels = new Object2ObjectOpenHashMap<>(this.topLevelModels);
        this.loadingStack = new ObjectOpenHashSet<>(this.loadingStack);
    }
}