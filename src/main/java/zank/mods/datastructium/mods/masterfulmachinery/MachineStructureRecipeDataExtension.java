package zank.mods.datastructium.mods.masterfulmachinery;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author ZZZank
 */
public interface MachineStructureRecipeDataExtension {

    @NotNull
    String[] dataStruct$getColonSplitTag();

    @Nullable
    ResourceLocation dataStruct$tagAsRL();

    @Nullable
    ResourceLocation dataStruct$blockId();
}
