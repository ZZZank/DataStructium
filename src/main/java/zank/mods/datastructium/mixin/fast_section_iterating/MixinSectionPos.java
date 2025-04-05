package zank.mods.datastructium.mixin.fast_section_iterating;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import zank.mods.datastructium.utils.SectionBlockPosIterator;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author ZZZank
 */
@Mixin(SectionPos.class)
public abstract class MixinSectionPos {

    @Shadow
    public abstract int minBlockX();

    @Shadow
    public abstract int minBlockY();

    @Shadow
    public abstract int minBlockZ();

    /**
     * @author ZZZank
     * @reason faster section iterator
     */
    @Overwrite
    public Stream<BlockPos> blocksInside() {
        return StreamSupport.stream(
            SectionBlockPosIterator.iterate(
                this.minBlockX(),
                this.minBlockY(),
                this.minBlockZ()
            ).spliterator(), false
        );
    }
}
