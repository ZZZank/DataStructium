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

    private static final float EPSILON = 0.0000001F;

    public static final int SHORT_OFFSET = 32768;
    public static final int MINIMUM = -32768;
    public static final int MAXIMUM = 32767;

    static {
        val start = System.currentTimeMillis();
        Arrays.setAll(TAG_FLOATS, i -> FloatTag.valueOf(i - SHORT_OFFSET));
        Arrays.setAll(TAG_DOUBLES, i -> DoubleTag.valueOf(i - SHORT_OFFSET));
    }

    public static @Nullable FloatTag getCachedFloat(final float f) {
        return !isFloatInteger(f) || f < MINIMUM || f > MAXIMUM
            ? null
            : TAG_FLOATS[(int) f + SHORT_OFFSET];
    }

    public static @Nullable DoubleTag getCachedDouble(final double d) {
        return !isDoubleInteger(d) || d < MINIMUM || d > MAXIMUM
            ? null
            : TAG_DOUBLES[(int) d + SHORT_OFFSET];
    }

    public static boolean isFloatInteger(float value) {
        return Math.abs(value - Math.round(value)) < EPSILON;
    }

    public static boolean isDoubleInteger(double value) {
        return Math.abs(value - Math.round(value)) < EPSILON;
    }
}
