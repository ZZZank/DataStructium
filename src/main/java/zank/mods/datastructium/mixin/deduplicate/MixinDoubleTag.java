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
        return CachedTags.ofDouble(data);
    }
}
