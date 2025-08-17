package zank.mods.datastructium.mixin.mods.masterfulmachinery.structure_check;

import com.ticticboooom.mods.mm.block.tile.ControllerBlockEntity;
import com.ticticboooom.mods.mm.block.tile.UpdatableTile;
import com.ticticboooom.mods.mm.data.MachineStructureRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zank.mods.datastructium.DSConfig;

/**
 * @author ZZZank
 */
@Mixin(ControllerBlockEntity.class)
public abstract class MixinControllerBlockEntity extends UpdatableTile {

    @Unique
    private int dataStructium$counter = 0;

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/RecipeManager;getAllRecipesFor(Lnet/minecraft/world/item/crafting/RecipeType;)Ljava/util/List;"
        ),
        cancellable = true
    )
    private void skipStructureSearch(CallbackInfo ci) {
        if (dataStructium$counter != 0) {
            this.update(); // this will happen regardless of `foundStructure`
            ci.cancel();
        }

        dataStructium$counter++;
        if (dataStructium$counter == DSConfig.MM_STRUCTURE_CHECK_INTERVAL) {
            dataStructium$counter = 0;
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lcom/ticticboooom/mods/mm/data/MachineStructureRecipe;matchesSpecificTransform(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/Level;I)Z"))
    private boolean skipStructureValidate(MachineStructureRecipe instance, BlockPos controllerPos, Level world, int index) {
        if (dataStructium$counter == 0) {
            return instance.matchesSpecificTransform(controllerPos, world, index);
        }
        return true;
    }

    private MixinControllerBlockEntity() {
        super(null);
    }
}
