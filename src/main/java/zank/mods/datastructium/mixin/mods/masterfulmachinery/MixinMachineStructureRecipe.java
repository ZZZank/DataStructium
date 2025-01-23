package zank.mods.datastructium.mixin.mods.masterfulmachinery;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.MachinePortBlock;
import com.ticticboooom.mods.mm.block.tile.IMachinePortTile;
import com.ticticboooom.mods.mm.data.MachineStructureRecipe;
import com.ticticboooom.mods.mm.data.model.structure.MachineStructureRecipeKeyModel;
import com.ticticboooom.mods.mm.helper.RLUtils;
import lombok.val;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;
import zank.mods.datastructium.mods.masterfulmachinery.MachineStructureRecipeDataExtension;

import java.util.List;

/**
 * @author ZZZank
 */
@Mixin(value = MachineStructureRecipe.class, remap = false)
@Pseudo
public abstract class MixinMachineStructureRecipe {

    @Shadow
    @Final
    private List<String> controllerId;

    /**
     * @author ZZZank
     * @reason cache result of string split and ResourceLocation construction
     */
    @Overwrite
    public boolean innerBlockMatch(BlockPos controllerPos, Level world, MachineStructureRecipeKeyModel model) {

        for (val data : model.getData()) {
            val pos = controllerPos.offset(model.getPos().getX(), model.getPos().getY(), model.getPos().getZ());
            val blockState = world.getBlockState(pos);

            boolean valid = false;

            if (!data.getTag().isEmpty()) {
                val extension = (MachineStructureRecipeDataExtension) data;
                if (extension.dataStruct$getColonSplitTag().length != 2) {
                    MM.LOG.fatal("too many : (colons) in structure tag: {}", data.getTag());
                    continue;
                }

                val tag = BlockTags.getAllTags().getTag(extension.dataStruct$tagAsRL());
                if (tag == null) {
                    MM.LOG.fatal("no existing block tag for structure tag: {}", data.getTag());
                    continue;
                }

                valid = tag.contains(blockState.getBlock());
            } else if (!data.getBlock().isEmpty()) {
                valid = data.getBlock().equals(blockState.getBlock().getRegistryName().toString());
            } else if (data.getPort() != null) {
                val port = data.getPort();
                val be = world.getBlockEntity(pos);

                if (be instanceof IMachinePortTile
                    && blockState.getBlock() instanceof MachinePortBlock
                ) {
                    val portTile = (IMachinePortTile) be;
                    val portBlock = ((MachinePortBlock) blockState.getBlock());
                    if (portTile.isInput() == port.isInput()
                        && portBlock.getPortTypeId().equals(RLUtils.toRL(port.getType()))
                    ) {
                        val controllerIds = port.getControllerId() != null
                            ? port.getControllerId()
                            : this.controllerId;
                        valid = controllerIds.contains(portBlock.getControllerId());
                    }
                }
            }

            if (!valid) {
                continue;
            }

            if (data.getProperties() == null) {
                return true;
            }

            for (val entry : data.getProperties().entrySet()) {
                for (val propertyEntry : blockState.getValues().entrySet()) {
                    if (propertyEntry.getKey().getName().equals(entry.getKey())) {
                        val o = propertyEntry.getKey().getValue(entry.getValue());
                        if (propertyEntry.getValue().equals(o.get())) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
