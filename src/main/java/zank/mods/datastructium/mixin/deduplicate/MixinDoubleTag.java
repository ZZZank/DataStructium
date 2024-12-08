package zank.mods.datastructium.mixin.deduplicate;

import lombok.val;
import net.minecraft.nbt.DoubleTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import zank.mods.datastructium.utils.CachedTags;

/**
 * @author ZZZank
 */
@Mixin(DoubleTag.class)
public class MixinDoubleTag {

    /**
     * @author ZZZank
     * @reason use a bigger cache
     */
    @Overwrite
    public static DoubleTag valueOf(double data) {
        val cached = CachedTags.getCachedDouble(data);
        return cached == null ? new DoubleTag(data) : cached;
    }
}
