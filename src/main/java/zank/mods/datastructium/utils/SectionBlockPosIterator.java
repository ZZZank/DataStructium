package zank.mods.datastructium.utils;

import lombok.val;
import net.minecraft.core.BlockPos;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * backported from ModernFix for MC 1.20, because ModernFix for 1.16 is EOL
 * @author embeddedt
 * @link <a href="https://github.com/embeddedt/ModernFix/blob/fcea407708a4a784c4076e080540d9264778f15a/common/src/main/java/org/embeddedt/modernfix/common/mixin/perf/chunk_meshing/RebuildTaskMixin.java">Source</a>
 */
public class SectionBlockPosIterator implements Iterator<BlockPos> {
    public static final int SECTION_SIZE = 1 << 4;
    public static final int SECTION_MASK = 0b1111;

    private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
    private int index = 0;
    private final int baseX, baseY, baseZ;

    public SectionBlockPosIterator(int baseX, int baseY, int baseZ) {
        this.baseX = baseX;
        this.baseY = baseY;
        this.baseZ = baseZ;
    }

    public SectionBlockPosIterator(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public boolean hasNext() {
        return index < SECTION_SIZE * SECTION_SIZE * SECTION_SIZE;
    }

    @Override
    public BlockPos next() {
        val i = index;
        if (i >= 4096) {
            throw new NoSuchElementException();
        }
        index = i + 1;
        val pos = this.pos;
        pos.set(
            this.baseX + (i & SECTION_MASK),
            this.baseY + ((i >> 8) & SECTION_MASK),
            this.baseZ + ((i >> 4) & SECTION_MASK)
        );
        return pos;
    }
}