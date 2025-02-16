package zank.mods.datastructium.mixin.mods.jer;

import com.google.common.base.Suppliers;
import jeresources.entry.MobEntry;
import jeresources.jei.mob.MobWrapper;
import lombok.val;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.spongepowered.asm.mixin.*;

import java.util.function.Supplier;

/**
 * @author ZZZank
 */
@Mixin(MobWrapper.class)
@Pseudo
public class MixinMobWrapper {
    @Shadow
    @Final
    private MobEntry mob;

    /**
     * @author ZZZank
     * @reason use forge's map based egg lookup, return empty stack if no spawn egg found
     */
    @Overwrite
    public ItemStack getSpawnEgg() {
        val item = ForgeSpawnEggItem.fromEntityType(this.mob.getEntity().getType());
        if (item == null) {
            return ItemStack.EMPTY;
        }
        return item.getDefaultInstance();
    }
}
