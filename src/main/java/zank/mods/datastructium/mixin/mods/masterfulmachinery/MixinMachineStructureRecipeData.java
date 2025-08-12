package zank.mods.datastructium.mixin.mods.masterfulmachinery;

import com.ticticboooom.mods.mm.data.model.structure.MachineStructureRecipeData;
import lombok.val;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import zank.mods.datastructium.mods.masterfulmachinery.MachineStructureRecipeDataExtension;

/**
 * @author ZZZank
 */
@Mixin(value = MachineStructureRecipeData.class, remap = false)
public abstract class MixinMachineStructureRecipeData implements MachineStructureRecipeDataExtension {
    @Unique
    private String[] dataStruct$colonSplitTag;

    @Unique
    private ResourceLocation dataStruct$tagRL;
    @Unique
    private boolean dataStruct$tagRLInitialized = false;

    @Unique
    private ResourceLocation dataStruct$blockId;
    @Unique
    private boolean dataStruct$blockIdInitialized = false;

    @Shadow
    @Final
    private String tag;

    @Shadow @Final private String block;

    @Override
    public @NotNull String[] dataStruct$getColonSplitTag() {
        if (dataStruct$colonSplitTag == null) {
            dataStruct$colonSplitTag = this.tag.split(":");
        }
        return dataStruct$colonSplitTag;
    }

    @Override
    @Nullable
    public ResourceLocation dataStruct$tagAsRL() {
        if (!dataStruct$tagRLInitialized) {
            dataStruct$tagRLInitialized = true;
            val splitTag = dataStruct$getColonSplitTag();
            try {
                dataStruct$tagRL = new ResourceLocation(splitTag[0], splitTag[1]);
            } catch (Exception e) {
                dataStruct$tagRL = null;
                throw e; // keep old behaviour
            }
        }
        return dataStruct$tagRL;
    }

    @Override
    public @Nullable ResourceLocation dataStruct$blockId() {
        if (!dataStruct$blockIdInitialized) {
            dataStruct$blockIdInitialized = true;
            dataStruct$blockId = ResourceLocation.tryParse(this.block);
        }
        return dataStruct$blockId;
    }
}
