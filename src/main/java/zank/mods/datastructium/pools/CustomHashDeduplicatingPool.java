package zank.mods.datastructium.pools;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import lombok.Getter;

/**
 * @author ZZZank
 */
public final class CustomHashDeduplicatingPool<T> {

    private final ObjectOpenCustomHashSet<T> pool;
    @Getter
    private long accessed;

    public CustomHashDeduplicatingPool(Hash.Strategy<T> strategy) {
        this(strategy, true);
    }

    public CustomHashDeduplicatingPool(Hash.Strategy<T> strategy, boolean recordAccess) {
        pool = new ObjectOpenCustomHashSet<>(strategy);
        accessed = recordAccess ? 0 : -1;
    }

    public T unique(T o) {
        if (accessed >= 0) {
            ++accessed;
        }
        return pool.addOrGet(o);
    }

    public void clear() {
        if (accessed >= 0) {
            accessed = 0;
        }
        pool.clear();
    }
}
