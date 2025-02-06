package zank.mods.datastructium.utils;

import lombok.val;
import net.minecraft.nbt.*;
import zank.mods.datastructium.DSConfig;

import java.util.Arrays;

/**
 * @author ZZZank
 */
public class CachedTags {

    public static final int START = DSConfig.NUMBER_TAG_CACHE_START;
    public static final int END = DSConfig.NUMBER_TAG_CACHE_END;
    public static final int SIZE = END - START;

    private static final FloatTag[] TAG_FLOATS = new FloatTag[SIZE];
    private static final DoubleTag[] TAG_DOUBLES = new DoubleTag[SIZE];
    private static final IntTag[] TAG_INTS = new IntTag[SIZE];

    private static final float EPSILON = 0.0000001F;
    /**
     * <a href="https://0.30000000000000004.com/">0.30000000000000004.com</a>
     */
    private static final double EPSILON_DOUBLE = 0.0000000000000004;

    static {
        val start = System.currentTimeMillis();
        Arrays.setAll(TAG_FLOATS, i -> new FloatTag(i + START));
        Arrays.setAll(TAG_DOUBLES, i -> new DoubleTag(i + START));
        Arrays.setAll(TAG_INTS, i -> new IntTag(i + START));
        val end = System.currentTimeMillis();
    }

    public static IntTag ofInt(final int i) {
        return i < START || i >= END
            ? new IntTag(i)
            : TAG_INTS[i - START];
    }

    public static FloatTag ofFloat(final float f) {
        return !isFloatInteger(f) || f < START || f >= END
            ? new FloatTag(f)
            : TAG_FLOATS[(int) f - START];
    }

    public static DoubleTag ofDouble(final double d) {
        return !isDoubleInteger(d) || d < START || d >= END
            ? new DoubleTag(d)
            : TAG_DOUBLES[(int) d - START];
    }

    public static boolean isFloatInteger(float value) {
        return Math.abs(value - Math.round(value)) < EPSILON;
    }

    public static boolean isDoubleInteger(double value) {
        return Math.abs(value - Math.round(value)) < EPSILON_DOUBLE;
    }
}
