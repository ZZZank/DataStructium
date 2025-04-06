package zank.mods.datastructium.mixin.disable_recipe_awarding;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

/**
 * @author ZZZank
 */
@Mixin(ServerPlayer.class)
public class MixinServerPlayer {

    @Inject(method = "awardRecipes", at = @At("HEAD"), cancellable = true)
    void cancelRecipeAwarding(Collection<Recipe<?>> recipes, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(0);
    }

    @Inject(method = "awardRecipesByKey", at = @At("HEAD"), cancellable = true)
    void cancelByKeyRecipeAwarding(ResourceLocation[] recipesKeys, CallbackInfo ci) {
        ci.cancel();
    }
}
