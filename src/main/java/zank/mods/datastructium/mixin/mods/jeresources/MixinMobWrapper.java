package zank.mods.datastructium.mixin.mods.jeresources;

import jeresources.entry.MobEntry;
import jeresources.jei.mob.MobWrapper;
import lombok.val;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.spongepowered.asm.mixin.*;

/**
 * @author ZZZank
 */
@Mixin(value = MobWrapper.class, remap = false)
@Pseudo
public class MixinMobWrapper {
    @Shadow
    @Final
    private MobEntry mob;

    /**
     * @author ZZZank
     * @reason use forge's map based egg lookup, return null stack if no spawn egg found
     * @see jeresources.jei.mob.MobCategory#setRecipe(IRecipeLayout, MobWrapper, IIngredients)
     */
    @Overwrite
    public ItemStack getSpawnEgg() {
        val item = ForgeSpawnEggItem.fromEntityType(this.mob.getEntity().getType());
        if (item == null) {
            return null;
        }
        return item.getDefaultInstance();
    }
}
