package test;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.BlockPos;
import org.junit.jupiter.api.Test;

/**
 * @author ZZZank
 */
public class VecHashingTest {

    @Test
    public void vanilla() {
        var set = new IntOpenHashSet();
        var collided = 0;
        for (var pos : BlockPos.betweenClosed(-100, -20, -100, 100, 50, 100)) {
            var hash = (pos.hashCode());
            if (!set.add(hash)) {
                collided++;
            }
        }
        System.out.printf("Vanilla hash algorithm: %s collisions, %s total hashcode(s)", collided, set.size());
        System.out.println();
    }

    @Test
    public void mixin() {
        var set = new IntOpenHashSet();
        var collided = 0;
        for (var pos : BlockPos.betweenClosed(-100, -20, -100, 100, 50, 100)) {
            var hash = (HashCommon.mix(HashCommon.mix(pos.getX()) + pos.getY()) + pos.getZ());
            if (!set.add(hash)) {
                collided++;
            }
        }
        System.out.printf("DataStructium hash algorithm: %s collisions, %s total hashcode(s)", collided, set.size());
        System.out.println();
    }
}
