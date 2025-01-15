package zank.mods.datastructium.pools;

import it.unimi.dsi.fastutil.ints.IntArrays;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import zank.mods.datastructium.utils.pool.AsyncPool;
import zank.mods.datastructium.utils.pool.CustomHashDeduplicatingPool;
import zank.mods.datastructium.utils.pool.DeduplicatingPool;

/**
 * @author ZZZank
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Pools {
    public static final AsyncPool<String> NAMESPACES = new AsyncPool<>("resource location namespaces");
    public static final AsyncPool<String> RL_PATHS = new AsyncPool<>("resource location paths");
    public static final DeduplicatingPool<String> TAG_KEYS = new DeduplicatingPool<>();
    public static final DeduplicatingPool<String> MODEL_PROPERTIES = new DeduplicatingPool<>();
    public static final DeduplicatingPool<ResourceLocation> REGISTRY_KEYS = new DeduplicatingPool<>();
    public static final DeduplicatingPool<ResourceLocation> REGISTRY_LOCATIONS = new DeduplicatingPool<>();

    public static final CustomHashDeduplicatingPool<int[]> QUADS = new CustomHashDeduplicatingPool<>(IntArrays.HASH_STRATEGY);
}
