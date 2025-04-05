package zank.mods.datastructium;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.val;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;

/**
 * @author ZZZank
 */
public final class DSConfig {
    public static final boolean DISABLE_ALL_MIXINS;
    public static final boolean CANONICALIZE_QUADS;
    public static final boolean CACHE_SHADER_UNIFORMS;
    public static final boolean TIERED_COMPOUND_TAG_INTERNAL;
    public static final boolean DEDUPLICATE_COMPOUND_TAG_KEYS;
    public static final int COMPOUND_TAG_RECONSTRUCT_THRESHOLD;
    public static final boolean CACHE_NUMBER_TAG;
    public static final int NUMBER_TAG_CACHE_START;
    public static final int NUMBER_TAG_CACHE_END;
    public static final boolean DISABLE_RECIPE_AWARDING;
    public static final boolean OPTIMIZE_SIMPLE_MODEL;
    public static final boolean REPLACE_BLOCK_POS_HASHING;

    static {
        val snapshot = readFromFile();
        DISABLE_ALL_MIXINS = snapshot.DISABLE_ALL_MIXINS;
        CANONICALIZE_QUADS = snapshot.CANONICALIZE_QUADS;
        CACHE_SHADER_UNIFORMS = snapshot.CACHE_SHADER_UNIFORMS;
        TIERED_COMPOUND_TAG_INTERNAL = snapshot.TIERED_COMPOUND_TAG_INTERNAL;
        DEDUPLICATE_COMPOUND_TAG_KEYS = snapshot.DEDUPLICATE_COMPOUND_TAG_KEYS;
        COMPOUND_TAG_RECONSTRUCT_THRESHOLD = snapshot.COMPOUND_TAG_RECONSTRUCT_THRESHOLD;
        NUMBER_TAG_CACHE_START = snapshot.NUMBER_TAG_CACHE_START;
        NUMBER_TAG_CACHE_END = snapshot.NUMBER_TAG_CACHE_END;
        CACHE_NUMBER_TAG = snapshot.CACHE_NUMBER_TAG;
        DISABLE_RECIPE_AWARDING = snapshot.DISABLE_RECIPE_AWARDING;
        OPTIMIZE_SIMPLE_MODEL = snapshot.OPTIMIZE_SIMPLE_MODEL;
        REPLACE_BLOCK_POS_HASHING = snapshot.REPLACE_BLOCK_POS_HASHING;
    }

    private DSConfig() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @AllArgsConstructor
    @NoArgsConstructor
    private static class Snapshot {
        @SerializedName("Disable All Mixins")
        public boolean DISABLE_ALL_MIXINS = false;
        @SerializedName("Canonicalize Quads")
        public boolean CANONICALIZE_QUADS = false;
        @SerializedName("Cache Shader Uniforms")
        public boolean CACHE_SHADER_UNIFORMS = true;
        @SerializedName("Tiered Compound Tag Internal")
        public boolean TIERED_COMPOUND_TAG_INTERNAL = true;
        @SerializedName("Deduplicate CompoundTag Keys")
        public boolean DEDUPLICATE_COMPOUND_TAG_KEYS = true;
        @SerializedName("Compound Tag Internal Reconstruct Threshold")
        public int COMPOUND_TAG_RECONSTRUCT_THRESHOLD = 5;
        @SerializedName("Cache Number Tag")
        public boolean CACHE_NUMBER_TAG = true;
        @SerializedName("Number Tag Cache starts at (inclusive)")
        public int NUMBER_TAG_CACHE_START = -4096;
        @SerializedName("Number Tag Cache ends at (exclusive)")
        public int NUMBER_TAG_CACHE_END = 4096;
        @SerializedName("Prevent the server from `awarding` recipe data to player recipe book")
        public boolean DISABLE_RECIPE_AWARDING = false;
        @SerializedName("Optimize simple model")
        public boolean OPTIMIZE_SIMPLE_MODEL = true;
        @SerializedName("Replace hashing algorithm of BlockPos with one with less collision")
        public boolean REPLACE_BLOCK_POS_HASHING = true;
    }

    @NotNull
    private static Snapshot readFromFile() {
        val path = FMLPaths.CONFIGDIR.get().resolve(String.format("%s-config.json", DataStructium.MOD_ID));
        if (!Files.exists(path)) {
            return new Snapshot();
        }
        try (val reader = Files.newBufferedReader(path)) {
            return DataStructium.GSON.fromJson(reader, Snapshot.class);
        } catch (IOException e) {
            DataStructium.LOGGER.error("unable to read config", e);
            return new Snapshot();
        }
    }

    static void save() {
        val snapshot = new Snapshot(
            DISABLE_ALL_MIXINS,
            CANONICALIZE_QUADS,
            CACHE_SHADER_UNIFORMS,
            TIERED_COMPOUND_TAG_INTERNAL,
            DEDUPLICATE_COMPOUND_TAG_KEYS,
            COMPOUND_TAG_RECONSTRUCT_THRESHOLD,
            CACHE_NUMBER_TAG,
            NUMBER_TAG_CACHE_START,
            NUMBER_TAG_CACHE_END,
            DISABLE_RECIPE_AWARDING,
            OPTIMIZE_SIMPLE_MODEL,
            REPLACE_BLOCK_POS_HASHING
        );
        val path = FMLPaths.CONFIGDIR.get().resolve(String.format("%s-config.json", DataStructium.MOD_ID));
        try (val writer = Files.newBufferedWriter(path)) {
            DataStructium.GSON.toJson(snapshot, writer);
        } catch (IOException e) {
            DataStructium.LOGGER.error("unable to write config", e);
        }
    }
}
