package zank.mods.datastructium;

import it.unimi.dsi.fastutil.ints.IntArrays;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.resources.ResourceLocation;

/**
 * @author ZZZank
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Pools {
    public static final DeduplicatingPool<String> NAMESPACES = new DeduplicatingPool<>();
    public static final DeduplicatingPool<String> RL_PATHS = new DeduplicatingPool<>();
    public static final DeduplicatingPool<String> TAG_KEYS = new DeduplicatingPool<>();
    public static final DeduplicatingPool<String> MODEL_PROPERTIES = new DeduplicatingPool<>();
    public static final DeduplicatingPool<ResourceLocation> REGISTRY_KEYS = new DeduplicatingPool<>();
    public static final DeduplicatingPool<ResourceLocation> REGISTRY_LOCATIONS = new DeduplicatingPool<>();

    public static final CustomHashDeduplicatingPool<int[]> QUADS = new CustomHashDeduplicatingPool<>(IntArrays.HASH_STRATEGY);
}
