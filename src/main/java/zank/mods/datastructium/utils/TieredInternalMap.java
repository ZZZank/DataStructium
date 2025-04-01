package zank.mods.datastructium.utils;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import zank.mods.datastructium.DSConfig;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TieredInternalMap<K, V> implements Map<K, V> {

    public TieredInternalMap() {
        internal = new Object2ObjectArrayMap<>();
    }

    public TieredInternalMap(@NotNull Map<K, V> map) {
        if (map.isEmpty()) {
            internal = new Object2ObjectArrayMap<>();
        } else if (map instanceof Object2ObjectMap) {
            internal = map;
        } else if (map.size() < DSConfig.COMPOUND_TAG_RECONSTRUCT_THRESHOLD) {
            // initialize key-value pairs by ourselves to avoid checking keys on initialization
            val keys = new Object[map.size()];
            val values = new Object[map.size()];
            int i = 0;
            for (val entry : map.entrySet()) {
                keys[i] = entry.getKey();
                values[i] = entry.getValue();
                i++;
            }
            internal = new Object2ObjectArrayMap<>(keys, values);
        } else {
            internal = new Object2ObjectOpenHashMap<>(map);
        }
    }

    @NotNull
    private Map<K, V> internal;

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

    private void checkSize() {
        if (this.size() == DSConfig.COMPOUND_TAG_RECONSTRUCT_THRESHOLD && internal instanceof Object2ObjectArrayMap) {
            internal = new Object2ObjectOpenHashMap<>(internal);
        }
    }

    @Override
    public V put(K key, V value) {
        checkSize();
        return internal.put(key, value);
    }

    @Override
    public V compute(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        checkSize();
        return internal.compute(key, remappingFunction);
    }

    @Override
    public V computeIfAbsent(K key, @NotNull Function<? super K, ? extends V> mappingFunction) {
        checkSize();
        return internal.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public V computeIfPresent(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        checkSize();
        return internal.computeIfPresent(key, remappingFunction);
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
    public @NotNull Set<Entry<K, V>> entrySet() {
        return internal.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return internal.equals(o);
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