package zank.mods.datastructium.mixin.mods.farmersdelight;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vectorwing.farmersdelight.crafting.ingredients.ToolIngredient;
import zank.mods.datastructium.utils.CollectUtils;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author ZZZank
 */
@Mixin(ToolIngredient.class)
@Pseudo
public class MixinToolIngredient {
    @Unique
    private static Collection<ItemStack> dataStruct$cachedStacks;

    /**
     * @author ZZZank
     * @reason cache ItemStack so that
     * <p>
     * 1. we don't need to create ItemStack every time a ToolIngredient instance is created, and
     * <p>
     * 2. we can use the same ItemStack instance for all ToolIngredient, reducing memory allocation
     */
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;map(Ljava/util/function/Function;)Ljava/util/stream/Stream;", ordinal = 0))
    private static Stream<ItemStack> cacheStacks(Stream<Item> instance, Function<?, ?> function) {
        if (dataStruct$cachedStacks == null) {
            dataStruct$cachedStacks = CollectUtils.mapToList(ForgeRegistries.ITEMS.getValues(), ItemStack::new);
        }
        return dataStruct$cachedStacks.stream();
    }
}
