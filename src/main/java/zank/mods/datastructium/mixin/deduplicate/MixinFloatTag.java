package zank.mods.datastructium.mixin.deduplicate;

import net.minecraft.nbt.FloatTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import zank.mods.datastructium.utils.CachedTags;

/**
 * @author ZZZank
 */
@Mixin(FloatTag.class)
public class MixinFloatTag {

    /**
     * @author ZZZank
     * @reason use a bigger cache
     */
    @Overwrite
    public static FloatTag valueOf(float data) {
        return CachedTags.ofFloat(data);
    }
}
