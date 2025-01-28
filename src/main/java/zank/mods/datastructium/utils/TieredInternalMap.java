package zank.mods.datastructium.utils;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.val;
import zank.mods.datastructium.DSConfig;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class TieredInternalMap<K, V> implements Map<K, V> {

    private Map<K, V> internal = new Object2ObjectArrayMap<>();

    @Override
    public int size() {
        return internal.size();
    }

    @Override
    public boolean isEmpty() {
        return internal.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return internal.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return internal.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return internal.get(key);
    }

    @Override
    public V put(K key, V value) {
        if (this.size() == DSConfig.COMPOUND_TAG_RECONSTRUCT_THRESHOLD) {
            internal = new Object2ObjectOpenHashMap<>(internal);
        }
        return internal.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return internal.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        if (internal instanceof Object2ObjectOpenHashMap) {
            internal = new Object2ObjectArrayMap<>();
        } else {
            internal.clear();
        }
    }

    @Nonnull
    @Override
    public Set<K> keySet() {
        return internal.keySet();
    }

    @Nonnull
    @Override
    public Collection<V> values() {
        return internal.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return internal.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        val m = (Map) o;
        return m.size() == this.size() && this.entrySet().containsAll(m.entrySet());
    }

    @Override
    public int hashCode() {
        return internal.hashCode();
    }

    @Override
    public String toString() {
        return internal.toString();
    }
}