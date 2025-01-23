package zank.mods.datastructium.mixin.industrialforegoing;

import com.buuz135.industrial.block.resourceproduction.tile.MaterialStoneWorkFactoryTile;
import com.buuz135.industrial.plugin.jei.JEICustomPlugin;
import com.buuz135.industrial.plugin.jei.category.StoneWorkCategory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import zank.mods.datastructium.mods.industrialforegoing.StoneWorkRecipeGatherer;

import java.util.List;

/**
 * @author ZZZank
 */
@Mixin(value = JEICustomPlugin.class, remap = false)
public abstract class MixinJEICustomPlugin {

    @Redirect(
        method = "getStoneWorkOutputFrom(Lnet/minecraft/world/item/ItemStack;Ljava/util/List;)Lnet/minecraft/world/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack cancelCopy(ItemStack instance) {
        return instance;
    }

    @Redirect(method = "registerRecipes", at = @At(value = "INVOKE", target = "Lcom/buuz135/industrial/plugin/jei/JEICustomPlugin;findAllStoneWorkOutputs(Lnet/minecraft/world/item/ItemStack;Ljava/util/List;)Ljava/util/List;"))
    private List<StoneWorkCategory.Wrapper> replaceStoneWorkRecipeGathering(
        JEICustomPlugin instance,
        ItemStack input,
        List<MaterialStoneWorkFactoryTile.StoneWorkAction> usedModes
    ) {
        return new StoneWorkRecipeGatherer().findAllStoneWorkOutputs(input, usedModes);
    }
}
