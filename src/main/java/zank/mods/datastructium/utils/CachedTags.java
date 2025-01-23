package zank.mods.datastructium.utils;

import lombok.val;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * @author ZZZank
 */
public class CachedTags {

    private static final FloatTag[] TAG_FLOATS = new FloatTag[65536];
    private static final DoubleTag[] TAG_DOUBLES = new DoubleTag[65536];
    private static final IntTag[] TAG_INTS = new IntTag[65536];

    private static final float EPSILON = 0.0000001F;
    /**
     * <a href="https://0.30000000000000004.com/">0.30000000000000004.com</a>
     */
    private static final double EPSILON_DOUBLE = 0.0000000000000004;

    public static final int SHORT_OFFSET = 32768;
    public static final int MINIMUM = -32768;
    public static final int MAXIMUM = 32767;

    static {
        val start = System.currentTimeMillis();
        Arrays.setAll(TAG_FLOATS, i -> new FloatTag(i - SHORT_OFFSET));
        Arrays.setAll(TAG_DOUBLES, i -> new DoubleTag(i - SHORT_OFFSET));
        Arrays.setAll(TAG_INTS, i -> new IntTag(i - SHORT_OFFSET));
        val end = System.currentTimeMillis();
    }

    public static IntTag ofInt(final int i) {
        return i < MINIMUM || i > MAXIMUM
            ? new IntTag(i)
            : TAG_INTS[i + SHORT_OFFSET];
    }

    public static FloatTag ofFloat(final float f) {
        return !isFloatInteger(f) || f < MINIMUM || f > MAXIMUM
            ? new FloatTag(f)
            : TAG_FLOATS[(int) f + SHORT_OFFSET];
    }

    public static DoubleTag ofDouble(final double d) {
        return !isDoubleInteger(d) || d < MINIMUM || d > MAXIMUM
            ? new DoubleTag(d)
            : TAG_DOUBLES[(int) d + SHORT_OFFSET];
    }

    public static boolean isFloatInteger(float value) {
        return Math.abs(value - Math.round(value)) < EPSILON;
    }

    public static boolean isDoubleInteger(double value) {
        return Math.abs(value - Math.round(value)) < EPSILON_DOUBLE;
    }
}
