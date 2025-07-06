package zank.mods.datastructium;

import lombok.val;
import net.minecraftforge.fml.loading.FMLPaths;
import zank.mods.datastructium.utils.SimpleConfig;

import java.io.IOException;
import java.nio.file.Files;

/**
 * @author ZZZank
 */
public final class DSConfig {
    public static final SimpleConfig CONFIG;

    public static final String CONFIG_FILE_NAME = String.format("%s-config.json", DataStructium.MOD_ID);

    public static final boolean DISABLE_ALL_MIXINS;
    public static final boolean CANONICALIZE_QUADS;
    public static final boolean CACHE_SHADER_UNIFORMS;
    public static final boolean COMPOUND_TAG_MODERNFIX;
    public static final boolean DEDUPLICATE_COMPOUND_TAG_KEYS;
    public static final int COMPOUND_TAG_RECONSTRUCT_THRESHOLD;
    public static final boolean CACHE_NUMBER_TAG;
    public static final int NUMBER_TAG_CACHE_START;
    public static final int NUMBER_TAG_CACHE_END;
    public static final boolean DISABLE_RECIPE_AWARDING;
    public static final boolean OPTIMIZE_SIMPLE_MODEL;
    public static final boolean REPLACE_VEC3I_HASHING;
    public static final boolean FAST_SECTION_ITERATING;
    public static final boolean DEDUPLICATE_INGREDIENT;

    static {
        val cfg = new SimpleConfig();
        CONFIG = cfg;
        read();
        DISABLE_ALL_MIXINS = cfg.getBool("Disable All Mixins", false);
        CANONICALIZE_QUADS = cfg.getBool("Canonicalize Quads", false);
        CACHE_SHADER_UNIFORMS = cfg.getBool("Cache Shader Uniforms", true);
        COMPOUND_TAG_MODERNFIX = cfg.getBool("Force disable CompoundTag optimization with ModernFix installed", true);
        DEDUPLICATE_COMPOUND_TAG_KEYS = cfg.getBool("Deduplicate CompoundTag Keys", true);
        COMPOUND_TAG_RECONSTRUCT_THRESHOLD = cfg.getInt("Compound Tag Internal Reconstruct Threshold", 5);
        NUMBER_TAG_CACHE_START = cfg.getInt("Number Tag Cache starts at (inclusive)", -2048);
        NUMBER_TAG_CACHE_END = cfg.getInt("Number Tag Cache ends at (exclusive)", 2048);
        CACHE_NUMBER_TAG = cfg.getBool("Cache Number Tag", true);
        DISABLE_RECIPE_AWARDING = cfg.getBool("Prevent the server from `awarding` recipe data to player recipe book", true);
        OPTIMIZE_SIMPLE_MODEL = cfg.getBool("Optimize simple model", true);
        REPLACE_VEC3I_HASHING = cfg.getBool("Replace hashing algorithm of Vec3i to reduce hash collision", true);
        FAST_SECTION_ITERATING = cfg.getBool("Faster chunk section iterating", true);
        DEDUPLICATE_INGREDIENT = cfg.getBool("Deduplicate Item Ingredient", false);
    }

    static void save() {
        val path = FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILE_NAME);
        try (val writer = Files.newBufferedWriter(path)) {
            CONFIG.write(DataStructium.GSON, writer);
        } catch (IOException e) {
            DataStructium.LOGGER.error("unable to write config", e);
        }
    }

    static void read() {
        val path = FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILE_NAME);
        if (!Files.exists(path)) {
            return;
        }
        try (val reader = Files.newBufferedReader(path)) {
            CONFIG.read(DataStructium.GSON, reader);
        } catch (IOException e) {
            DataStructium.LOGGER.error("unable to read config", e);
        }
    }

    private DSConfig() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
