package zank.mods.datastructium.utils;

import lombok.val;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author ZZZank
 */
public final class Lazy<T> implements Supplier<T> {

    public static <T> Lazy<T> of(Supplier<T> supplier) {
        if (supplier instanceof Lazy) {
            val lazy = (Lazy<T>) supplier;
            return new Lazy<>(lazy.supplier, lazy.cached, lazy.initialized);
        }
        return new Lazy<>(Objects.requireNonNull(supplier), null, false);
    }

    private final Supplier<T> supplier;
    private T cached;
    private boolean initialized;

    private Lazy(Supplier<T> supplier, T cached, boolean initialized) {
        this.supplier = supplier;
        this.cached = cached;
        this.initialized = initialized;
    }

    @Override
    public T get() {
        if (!initialized) {
            initialized = true;
            cached = supplier.get();
        }
        return cached;
    }

    public void forget() {
        cached = null;
        initialized = false;
    }
}
