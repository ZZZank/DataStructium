package zank.mods.datastructium.mixin.deduplicate;

import lombok.val;
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
        val cached = CachedTags.getCachedFloat(data);
        return cached == null ? new FloatTag(data) : cached;
    }
}
