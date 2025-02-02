package zank.mods.datastructium.mods.industrialforegoing;

import com.buuz135.industrial.block.resourceproduction.tile.MaterialStoneWorkFactoryTile;
import com.buuz135.industrial.plugin.jei.category.StoneWorkCategory;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ZZZank
 */
public final class StoneWorkRecipeGatherer {
    private final MaterialStoneWorkFactoryTile.StoneWorkAction[] actions = Arrays
        .stream(MaterialStoneWorkFactoryTile.ACTION_RECIPES)
        .filter(ac -> !ac.getAction().equals("none"))
        .toArray(MaterialStoneWorkFactoryTile.StoneWorkAction[]::new);
    private final List<StoneWorkCategory.Wrapper> toFill = new ArrayList<>();

    private void fillStoneWorkRecipe(
        ItemStack input,
        ItemStack lastOutput,
        List<MaterialStoneWorkFactoryTile.StoneWorkAction> usedModes
    ) {
        if (usedModes.size() >= 4) {
            return;
        }
        for (val mode : actions) {
            val output = this.applyWork(lastOutput, mode);

            if (!output.isEmpty()) {
                val usedModesStepped =
                    new ArrayList<MaterialStoneWorkFactoryTile.StoneWorkAction>(usedModes.size() + 1);
                usedModesStepped.addAll(usedModes);
                usedModesStepped.add(mode);

                toFill.add(new StoneWorkCategory.Wrapper(
                    input,
                    new ArrayList<>(usedModesStepped),
                    output.copy()
                ));
                fillStoneWorkRecipe(input, output, usedModesStepped);
            }
        }
    }

    public List<StoneWorkCategory.Wrapper> findAllStoneWorkOutputs(
        ItemStack input,
        List<MaterialStoneWorkFactoryTile.StoneWorkAction> usedModes
    ) {
        if (toFill.isEmpty()) {
            fillStoneWorkRecipe(input, input, usedModes);
        }
        return toFill;
    }

    private ItemStack applyWork(ItemStack stack, MaterialStoneWorkFactoryTile.StoneWorkAction mode) {
        return mode.getWork().apply(Minecraft.getInstance().level, ItemHandlerHelper.copyStackWithSize(stack, 9));
    }
}
