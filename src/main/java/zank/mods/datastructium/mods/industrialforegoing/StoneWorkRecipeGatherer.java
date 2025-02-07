package zank.mods.datastructium.mods.industrialforegoing;

import com.buuz135.industrial.block.resourceproduction.tile.MaterialStoneWorkFactoryTile;
import com.buuz135.industrial.block.resourceproduction.tile.MaterialStoneWorkFactoryTile.StoneWorkAction;
import com.buuz135.industrial.plugin.jei.category.StoneWorkCategory;
import com.hrznstudio.titanium.container.BasicContainer;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author ZZZank
 */
@UtilityClass
public class StoneWorkRecipeGatherer {

    private static StoneWorkAction[] ACTIONS;

    private static List<SmeltingRecipe> SMELT;
    private static List<CraftingRecipe> SMALL_CRAFT;
    private static List<CraftingRecipe> BIG_CRAFT;

    public static void init(Level level) {
        ACTIONS = Arrays.stream(MaterialStoneWorkFactoryTile.ACTION_RECIPES)
            .filter(action -> !Objects.equals(action.getAction(), "none"))
            .toArray(StoneWorkAction[]::new);

        SMELT = level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING);

        val craftingRecipes = level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
        SMALL_CRAFT = craftingRecipes
            .stream()
            .filter(r -> r.canCraftInDimensions(2, 2))
            .collect(Collectors.toList());
        BIG_CRAFT = craftingRecipes
            .stream()
            .filter(r -> r.canCraftInDimensions(3, 3))
            .collect(Collectors.toList());
    }

    public static void clear() {
        ACTIONS = null;
        SMELT = null;
        SMALL_CRAFT = null;
        BIG_CRAFT = null;
    }

    public static SmeltingRecipe findSmelt(Level level, ItemStack stack) {
        if (SMELT == null) {
            return null;
        }
        for (val recipe : SMELT) {
            if (recipe.getIngredients().get(0).test(stack)) {
                return recipe;
            }
        }
        return null;
    }

    public static CraftingRecipe findSmallCraft(Level level, ItemStack stack) {
        if (SMALL_CRAFT == null) {
            return null;
        }
        val container = genCraftingInventory(stack, 2, 2);
        for (val recipe : SMALL_CRAFT) {
            if (recipe.matches(container, level)) {
                return recipe;
            }
        }
        return null;
    }

    public static CraftingRecipe findBigCraft(Level level, ItemStack stack) {
        if (BIG_CRAFT == null) {
            return null;
        }
        val container = genCraftingInventory(stack, 3, 3);
        for (val recipe : BIG_CRAFT) {
            if (recipe.matches(container, level)) {
                return recipe;
            }
        }
        return null;
    }

    private static BiFunction<Level, ItemStack, ItemStack> getWork(StoneWorkAction mode) {
        switch (mode.getAction()) {
            case "smelt":
                return (level, stack) -> {
                    val recipe = findSmelt(level, stack);
                    return recipe == null ? ItemStack.EMPTY : recipe.getResultItem();
                };
            case "small_craft":
                return (level, stack) -> {
                    val recipe = findSmallCraft(level, stack);
                    return recipe == null ? ItemStack.EMPTY : recipe.getResultItem();
                };
            case "big_craft":
                return (level, stack) -> {
                    val recipe = findBigCraft(level, stack);
                    return recipe == null ? ItemStack.EMPTY : recipe.getResultItem();
                };
        }
        return mode.getWork();
    }

    public static CraftingContainer genCraftingInventory(List<ItemStack> inputs, int width, int height) {
        val container = new CraftingContainer(new BasicContainer(MenuType.CRAFTING, 0), width, height);

        val size = Integer.min(width * height, inputs.size());
        for (int i = 0; i < size; i++) {
            container.setItem(i, inputs.get(i));
        }

        return container;
    }

    public static CraftingContainer genCraftingInventory(ItemStack fillInput, int width, int height) {
        return genCraftingInventory(Collections.nCopies(width * height, fillInput), width, height);
    }

    public static List<StoneWorkCategory.Wrapper> collectStoneWorks(
        ItemStack input,
        List<StoneWorkAction> usedModes
    ) {
        return new ArrayList<>(collectStoneWorks(input, input, usedModes));
    }

    private static List<StoneWorkCategory.Wrapper> collectStoneWorks(
        ItemStack input,
        ItemStack lastOutput,
        List<StoneWorkAction> usedModes
    ) {
        if (usedModes.size() >= 4) {
            return Collections.emptyList();
        }
        val recipes = new ArrayList<StoneWorkCategory.Wrapper>();
        for (val mode : ACTIONS) {
            val output =
                getWork(mode).apply(Minecraft.getInstance().level, ItemHandlerHelper.copyStackWithSize(lastOutput, 9));
            if (output.isEmpty()) {
                continue;
            }

            val usedModesStepped = new ArrayList<StoneWorkAction>(usedModes.size() + 1);
            usedModesStepped.addAll(usedModes);
            usedModesStepped.add(mode);

            recipes.add(new StoneWorkCategory.Wrapper(input, new ArrayList<>(usedModesStepped), output.copy()));
            recipes.addAll(collectStoneWorks(input, output, usedModesStepped));
        }
        return recipes;
    }
}
