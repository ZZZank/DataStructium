package zank.mods.datastructium.utils;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author ZZZank
 */
public class InternedTieredMap<V> extends TieredInternalMap<String, V> {

    private static final Interner<String> INTERNER = Interners.newWeakInterner();

    private static String intern(String key) {
        return key != null ? INTERNER.intern(key) : null;
    }

    @Override
    public V put(String key, V value) {
        return super.put(intern(key), value);
    }

    @Override
    public V compute(String key, @NotNull BiFunction<? super String, ? super V, ? extends V> remappingFunction) {
        return super.compute(intern(key), remappingFunction);
    }

    @Override
    public V computeIfAbsent(String key, @NotNull Function<? super String, ? extends V> mappingFunction) {
        return super.computeIfAbsent(intern(key), mappingFunction);
    }

    @Override
    public V computeIfPresent(
        String key,
        @NotNull BiFunction<? super String, ? super V, ? extends V> remappingFunction
    ) {
        return super.computeIfPresent(intern(key), remappingFunction);
    }
}
