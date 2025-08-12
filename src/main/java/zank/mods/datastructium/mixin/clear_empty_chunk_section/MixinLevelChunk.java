package zank.mods.datastructium.mixin.clear_empty_chunk_section;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.TickList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkBiomeContainer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * <a href="https://github.com/CaffeineMC/hydrogen-fabric/blob/1.16.x/src/main/java/me/jellysquid/mods/hydrogen/mixin/chunk/MixinWorldChunk.java">From Hydrogen</a>
 *
 * @author ZZZank
 */
@Mixin(LevelChunk.class)
public class MixinLevelChunk {
    @Shadow
    @Final
    private LevelChunkSection[] sections;

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/chunk/ChunkBiomeContainer;Lnet/minecraft/world/level/chunk/UpgradeData;Lnet/minecraft/world/level/TickList;Lnet/minecraft/world/level/TickList;J[Lnet/minecraft/world/level/chunk/LevelChunkSection;Ljava/util/function/Consumer;)V", at = @At("RETURN"))
    private void reinit(
        Level arg,
        ChunkPos arg2,
        ChunkBiomeContainer arg3,
        UpgradeData arg4,
        TickList<Block> arg5,
        TickList<Fluid> arg6,
        long l,
        @Nullable LevelChunkSection[] args,
        @Nullable Consumer<LevelChunk> consumer,
        CallbackInfo ci
    ) {
        // Upgrading a ProtoChunk to a WorldChunk might result in empty sections being copied over
        // These simply waste memory, and the WorldChunk will return air blocks for any absent section without issue.
        var sections = this.sections;
        for (int i = 0; i < sections.length; i++) {
            if (LevelChunkSection.isEmpty(sections[i])) {
                sections[i] = null;
            }
        }
    }
}
