package zank.mods.datastructium.mixin.fast_section_iterating;

import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import zank.mods.datastructium.utils.SectionBlockPosIterator;

@Mixin(targets = {"net/minecraft/client/renderer/chunk/ChunkRenderDispatcher$RenderChunk$RebuildTask"}, priority = 2000)
public abstract class MixinRebuildTask {
    /**
     * @author embeddedt
     * @reason Use a much faster iterator implementation than vanilla's Guava-based one.
     */
    @Redirect(method = "compile", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;betweenClosed(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)Ljava/lang/Iterable;"))
    private Iterable<BlockPos> fastBetweenClosed(BlockPos firstPos, BlockPos secondPos) {
        return SectionBlockPosIterator.iterate(firstPos.getX(), firstPos.getY(), firstPos.getZ());
    }
}