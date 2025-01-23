package zank.mods.datastructium.mixin.deduplicate;

import net.minecraft.nbt.IntTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import zank.mods.datastructium.utils.CachedTags;

/**
 * @author ZZZank
 */
@Mixin(IntTag.class)
public abstract class MixinIntTag {

    /**
     * @author ZZZank
     * @reason use a bigger cache
     */
    @Overwrite
    public static IntTag valueOf(int data) {
        return CachedTags.ofInt(data);
    }
}
