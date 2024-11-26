package zank.mods.datastructium.pools;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;

/**
 * @author ZZZank
 */
public final class DeduplicatingPool<T> {

    private final ObjectOpenHashSet<T> pool = new ObjectOpenHashSet<>();
    @Getter
    private long accessed;

    public DeduplicatingPool() {
        this(true);
    }

    public DeduplicatingPool(boolean recordAccess) {
        accessed = recordAccess ? 0 : -1;
    }

    public T unique(T o) {
        if (accessed >= 0) {
            ++accessed;
        }
        return pool.addOrGet(o);
    }

    public final void clear() {
        if (accessed >= 0) {
            accessed = 0;
        }
        pool.clear();
    }
}
