package zank.mods.datastructium.mods.industrialforegoing;

import com.buuz135.industrial.block.resourceproduction.tile.MaterialStoneWorkFactoryTile;
import com.buuz135.industrial.plugin.jei.category.StoneWorkCategory;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author ZZZank
 */
public class StoneWorkRecipeGatherer {
    private final MaterialStoneWorkFactoryTile.StoneWorkAction[] actions;
    private final ClientLevel level;

    public StoneWorkRecipeGatherer() {
        actions = Arrays.stream(MaterialStoneWorkFactoryTile.ACTION_RECIPES)
            .filter(ac -> !ac.getAction().equals("none"))
            .toArray(MaterialStoneWorkFactoryTile.StoneWorkAction[]::new);
        level = Minecraft.getInstance().level;
    }

    public List<StoneWorkCategory.Wrapper> findAllStoneWorkOutputs(
        ItemStack parent,
        List<MaterialStoneWorkFactoryTile.StoneWorkAction> usedModes
    ) {
        return findAllStoneWorkOutputs(parent, parent, usedModes);
    }

    public List<StoneWorkCategory.Wrapper> findAllStoneWorkOutputs(
        ItemStack input,
        ItemStack lastOutput,
        List<MaterialStoneWorkFactoryTile.StoneWorkAction> usedModes
    ) {
        if (usedModes.size() >= 4) {
            return Collections.emptyList();
        }
        val wrappers = new ArrayList<StoneWorkCategory.Wrapper>();
        for (val mode : actions) {
            val output = this.applyWork(lastOutput, mode);

            if (!output.isEmpty()) {
                val usedModesStepped =
                    new ArrayList<MaterialStoneWorkFactoryTile.StoneWorkAction>(usedModes.size() + 1);
                usedModesStepped.addAll(usedModes);
                usedModesStepped.add(mode);

                wrappers.add(new StoneWorkCategory.Wrapper(
                    input,
                    new ArrayList<>(usedModesStepped),
                    output.copy()
                ));
                wrappers.addAll(this.findAllStoneWorkOutputs(input, output, usedModesStepped));
            }
        }
        return wrappers;
    }

    private ItemStack applyWork(ItemStack stack, MaterialStoneWorkFactoryTile.StoneWorkAction mode) {
        return mode.getWork().apply(level, ItemHandlerHelper.copyStackWithSize(stack, 9));
    }
}
