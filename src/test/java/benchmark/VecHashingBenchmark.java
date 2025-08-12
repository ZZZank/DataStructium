package benchmark;

import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.core.BlockPos;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author ZZZank
 */
public class VecHashingBenchmark {

    @Benchmark
    public void vanilla(Blackhole bh) {
        for (var pos : BlockPos.betweenClosed(-100, -20, -100, 100, 50, 100)) {
            bh.consume(pos.hashCode());
        }
    }

    @Benchmark
    public void mixin(Blackhole bh) {
        for (var pos : BlockPos.betweenClosed(-100, -20, -100, 100, 50, 100)) {
            bh.consume(HashCommon.mix(HashCommon.mix(pos.getX()) + pos.getY()) + pos.getZ());
        }
    }

    public static void main(String[] args) throws RunnerException {
        var option = new OptionsBuilder()
            .include(VecHashingBenchmark.class.getSimpleName())
            .warmupIterations(2)
            .forks(1)
            .build();
        new Runner(option).run();
    }
}
