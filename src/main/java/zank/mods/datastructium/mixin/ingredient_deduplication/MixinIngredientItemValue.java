package zank.mods.datastructium.mixin.ingredient_deduplication;


import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Ingredient.ItemValue.class)
public abstract class MixinIngredientItemValue {
    /**
     * @author embeddedt
     * @reason Defensively copy the item so that the deduplication is not visible to most mods (unless they introspect
     * the item held within this object directly). This is necessary since some mods edit the returned stack.
     */
    @Redirect(method = "getItems", at = @At(value = "FIELD", target = "Lnet/minecraft/world/item/crafting/Ingredient$ItemValue;item:Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack dataStruct$defensiveCopy(Ingredient.ItemValue instance) {
        return instance.item.copy();
    }
}
