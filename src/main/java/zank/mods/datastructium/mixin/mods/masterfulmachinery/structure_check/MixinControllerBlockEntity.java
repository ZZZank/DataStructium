package zank.mods.datastructium.mixin.mods.masterfulmachinery.structure_check;

import com.ticticboooom.mods.mm.block.tile.ControllerBlockEntity;
import com.ticticboooom.mods.mm.data.MachineStructureRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
public abstract class MixinControllerBlockEntity {

    @Shadow
    protected abstract void onStructureFound(MachineStructureRecipe structure, int index);

    @Unique
    private int dataStructium$counter = 0;

    @Inject(method = "tick", at = @At("RETURN"))
    private void updateCounter(CallbackInfo ci) {
        dataStructium$counter++;
        if (dataStructium$counter >= DSConfig.MM_STRUCTURE_CHECK_INTERVAL) {
            dataStructium$counter = 0;
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lcom/ticticboooom/mods/mm/data/MachineStructureRecipe;matchesSpecificTransform(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/Level;I)Z"))
    private boolean addIntervalToStructureValidation(
        MachineStructureRecipe instance,
        BlockPos controllerPos,
        Level world,
        int index
    ) {
        if (dataStructium$counter == 0) {
            return instance.matchesSpecificTransform(controllerPos, world, index);
        }
        return true;
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lcom/ticticboooom/mods/mm/data/MachineStructureRecipe;matchesAnyTransform(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/Level;Ljava/lang/String;)I"))
    private int addIntervalToStructureSearch(
        MachineStructureRecipe instance,
        BlockPos blockPos,
        Level world,
        String controllerIO
    ) {
        if (dataStructium$counter == 0) {
            return instance.matchesAnyTransform(blockPos, world, controllerIO);
        }
        return -1;
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lcom/ticticboooom/mods/mm/block/tile/ControllerBlockEntity;onStructureFound(Lcom/ticticboooom/mods/mm/data/MachineStructureRecipe;I)V", ordinal = 0))
    private void addIntervalToOnStructureFound(
        ControllerBlockEntity instance,
        MachineStructureRecipe structureRecipe,
        int index
    ) {
        if (dataStructium$counter == 0) {
            this.onStructureFound(structureRecipe, index);
        }
    }
}
