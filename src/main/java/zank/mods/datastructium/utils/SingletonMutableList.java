package zank.mods.datastructium.utils;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * @author ZZZank
 */
public final class SingletonMutableList<E> extends AbstractList<E> implements RandomAccess, Serializable {
    private E element;

    public SingletonMutableList(E element) {
        this.element = element;
    }

    @Override
    public E get(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: 1");
        }
        return element;
    }

    @Override
    public E set(int index, E element) {
        if (index != 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: 1");
        }
        var old = this.element;
        this.element = element;
        return old;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean contains(Object o) {
        return Objects.equals(o, this.element);
    }

    @Override
    public int indexOf(Object o) {
        return Objects.equals(o, this.element) ? 0 : -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        return Objects.equals(o, this.element) ? 0 : -1;
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        action.accept(element);
    }

    @Override
    public void replaceAll(@NotNull UnaryOperator<E> operator) {
        this.element = operator.apply(this.element);
    }

    @Override
    public void sort(Comparator<? super E> c) {
    }

    @Override
    public @NotNull Spliterator<E> spliterator() {
        return new Spliterator<>() {
            long est = 1;

            @Override
            public Spliterator<E> trySplit() {
                return null;
            }

            @Override
            public boolean tryAdvance(Consumer<? super E> consumer) {
                Objects.requireNonNull(consumer);
                if (est > 0) {
                    est--;
                    consumer.accept(element);
                    return true;
                }
                return false;
            }

            @Override
            public void forEachRemaining(Consumer<? super E> action) {
                tryAdvance(action);
            }

            @Override
            public long estimateSize() {
                return est;
            }

            @Override
            public int characteristics() {
                int value = (element != null) ? Spliterator.NONNULL : 0;

                return value | Spliterator.SIZED | Spliterator.SUBSIZED |
                    Spliterator.DISTINCT | Spliterator.ORDERED;
            }
        };
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.element);
    }
}
