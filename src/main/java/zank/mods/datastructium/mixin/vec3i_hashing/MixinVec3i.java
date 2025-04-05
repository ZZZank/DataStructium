package zank.mods.datastructium.mixin.vec3i_hashing;

import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.core.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * applying this hashing change to its subclass {@link net.minecraft.core.BlockPos} is a bad idea,
 * because some mods (Yes, I'm talking about you, Immersive Engineering) might use BlockPos as
 * {@link Vec3i}, then two same {@link Vec3i} with the same value will have different hashcode
 *
 * @author ZZZank
 */
@Mixin(Vec3i.class)
public abstract class MixinVec3i {

    @Shadow
    private int x;

    @Shadow
    private int y;

    @Shadow
    private int z;

    @Override
    public int hashCode() {
        return HashCommon.mix(HashCommon.mix(this.x) + this.y) + this.z;
    }
}
