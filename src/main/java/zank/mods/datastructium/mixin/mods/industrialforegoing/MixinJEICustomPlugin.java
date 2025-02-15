package zank.mods.datastructium.mixin.mods.industrialforegoing;

import com.buuz135.industrial.block.resourceproduction.tile.MaterialStoneWorkFactoryTile;
import com.buuz135.industrial.plugin.jei.JEICustomPlugin;
import com.buuz135.industrial.plugin.jei.category.StoneWorkCategory;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import zank.mods.datastructium.mods.industrialforegoing.StoneWorkRecipeGatherer;

import java.util.List;

/**
 * @author ZZZank
 */
@Mixin(value = JEICustomPlugin.class)
@Pseudo
public abstract class MixinJEICustomPlugin {

    @Redirect(
        method = "registerRecipes",
        at = @At(
            value = "INVOKE",
            target = "Lcom/buuz135/industrial/plugin/jei/JEICustomPlugin;findAllStoneWorkOutputs(Lnet/minecraft/world/item/ItemStack;Ljava/util/List;)Ljava/util/List;"
        )
    )
    private List<StoneWorkCategory.Wrapper> replaceStoneWorkRecipeGathering(
        JEICustomPlugin instance,
        ItemStack input,
        List<MaterialStoneWorkFactoryTile.StoneWorkAction> usedModes
    ) {
        assert Minecraft.getInstance().level != null;
        StoneWorkRecipeGatherer.init(Minecraft.getInstance().level);
        val collected = StoneWorkRecipeGatherer.collectStoneWorks(input, usedModes);
        StoneWorkRecipeGatherer.clear();
        return collected;
    }
}
