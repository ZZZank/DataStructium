package zank.mods.datastructium.utils;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * from ModernFix, thank you embeddedt
 * @author ZZZank
 */
public class IngredientDeduplicator {

    private static final ObjectOpenCustomHashSet<Ingredient.ItemValue> VALUES =
        new ObjectOpenCustomHashSet<>(ItemValueEqualStrategy.INSTANCE);

    public static Ingredient.ItemValue deduplicate(Ingredient.ItemValue value) {
        synchronized (VALUES) {
            return VALUES.addOrGet(value);
        }
    }

    private enum ItemValueEqualStrategy implements Hash.Strategy<Ingredient.ItemValue> {
        INSTANCE;

        @Override
        public int hashCode(Ingredient.ItemValue o) {
            if (o == null) {
                return 0;
            }
            var item = o.item;
            if (item.hasTag()) {
                return item.getItem().hashCode() + 31 * item.getTag().hashCode();
            }
            return item.getItem().hashCode();
        }

        @Override
        public boolean equals(Ingredient.ItemValue a, Ingredient.ItemValue b) {
            return a == b || a != null && b != null && ItemStack.matches(a.item, b.item);
        }
    }
}
