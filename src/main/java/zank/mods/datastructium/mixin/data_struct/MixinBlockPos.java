package zank.mods.datastructium.mixin.data_struct;

import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author ZZZank
 */
@Mixin(BlockPos.class)
public abstract class MixinBlockPos extends Vec3i {

    @Override
    public int hashCode() {
        return HashCommon.mix(HashCommon.mix(this.getX()) + this.getY()) + this.getZ();
    }

    MixinBlockPos() {
        super(0, 0, 0);
    }
}
