package zank.mods.datastructium.mixin.mods.masterfulmachinery.structure_check;

import com.ticticboooom.mods.mm.block.tile.ControllerBlockEntity;
import com.ticticboooom.mods.mm.block.tile.UpdatableTile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
            value = "FIELD",
            target = "Lcom/ticticboooom/mods/mm/block/tile/ControllerBlockEntity;processData:Lcom/ticticboooom/mods/mm/model/ProcessUpdate;",
            ordinal = 0,
            remap = false
        ),
        cancellable = true
    )
    private void skipActionByInterval(CallbackInfo ci) {
        if (dataStructium$counter != 0) {
            ci.cancel();
            this.update(); // this will happen regardless of `foundStructure`
        }

        dataStructium$counter++;
        if (dataStructium$counter == DSConfig.MM_STRUCTURE_CHECK_INTERVAL) {
            dataStructium$counter = 0;
        }
    }

    private MixinControllerBlockEntity() {
        super(null);
    }
}
