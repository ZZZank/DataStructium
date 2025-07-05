package zank.mods.datastructium.mixin.deduplicate_ingredient;

import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import zank.mods.datastructium.utils.IngredientDeduplicator;

import java.util.stream.Stream;

/**
 * @author ZZZank
 */
@Mixin(Ingredient.class)
public abstract class MixinIngredient {

    @ModifyVariable(method = "<init>", at = @At(value = "HEAD"), argsOnly = true)
    private static Stream<? extends Ingredient.Value> ds$deduplicateValue(Stream<? extends Ingredient.Value> stream) {
        return stream.map(value -> value.getClass() == Ingredient.ItemValue.class
            ? IngredientDeduplicator.deduplicate((Ingredient.ItemValue) value)
            : value);
    }
}
