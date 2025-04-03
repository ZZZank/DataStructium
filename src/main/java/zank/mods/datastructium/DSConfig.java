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
    public static final boolean CANONICALIZE_QUADS;
    public static final boolean CACHE_SHADER_UNIFORMS;
    public static final boolean TIERED_COMPOUND_TAG_INTERNAL;
    public static final int COMPOUND_TAG_RECONSTRUCT_THRESHOLD;
    public static final boolean CACHE_NUMBER_TAG;
    public static final int NUMBER_TAG_CACHE_START;
    public static final int NUMBER_TAG_CACHE_END;
    public static final boolean DISABLE_RECIPE_AWARDING;
    public static final boolean OPTIMIZE_SIMPLE_MODEL;

    static {
        val snapshot = readFromFile();
        CANONICALIZE_QUADS = snapshot.CANONICALIZE_QUADS;
        CACHE_SHADER_UNIFORMS = snapshot.CACHE_SHADER_UNIFORMS;
        COMPOUND_TAG_RECONSTRUCT_THRESHOLD = snapshot.COMPOUND_TAG_RECONSTRUCT_THRESHOLD;
        TIERED_COMPOUND_TAG_INTERNAL = snapshot.TIERED_COMPOUND_TAG_INTERNAL;
        NUMBER_TAG_CACHE_START = snapshot.NUMBER_TAG_CACHE_START;
        NUMBER_TAG_CACHE_END = snapshot.NUMBER_TAG_CACHE_END;
        CACHE_NUMBER_TAG = snapshot.CACHE_NUMBER_TAG;
        DISABLE_RECIPE_AWARDING = snapshot.DISABLE_RECIPE_AWARDING;
        OPTIMIZE_SIMPLE_MODEL = snapshot.OPTIMIZE_SIMPLE_MODEL;
    }

    private DSConfig() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @AllArgsConstructor
    @NoArgsConstructor
    private static class Snapshot {
        @SerializedName("Canonicalize Quads")
        public boolean CANONICALIZE_QUADS = false;
        @SerializedName("Cache Shader Uniforms")
        public boolean CACHE_SHADER_UNIFORMS = true;
        @SerializedName("Tiered Compound Tag Internal")
        public boolean TIERED_COMPOUND_TAG_INTERNAL = true;
        @SerializedName("Compound Tag Internal Reconstruct Threshold")
        public int COMPOUND_TAG_RECONSTRUCT_THRESHOLD = 5;
        @SerializedName("Cache Number Tag")
        public boolean CACHE_NUMBER_TAG = true;
        @SerializedName("Number Tag Cache starts at (inclusive)")
        public int NUMBER_TAG_CACHE_START = -4096;
        @SerializedName("Number Tag Cache ends at (exclusive)")
        public int NUMBER_TAG_CACHE_END = 4096;
        @SerializedName("Prevent the server from 'awarding' recipe data to player recipe book")
        public boolean DISABLE_RECIPE_AWARDING = false;
        @SerializedName("Optimize simple model")
        public boolean OPTIMIZE_SIMPLE_MODEL = true;
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
            CANONICALIZE_QUADS,
            CACHE_SHADER_UNIFORMS,
            TIERED_COMPOUND_TAG_INTERNAL,
            COMPOUND_TAG_RECONSTRUCT_THRESHOLD,
            CACHE_NUMBER_TAG,
            NUMBER_TAG_CACHE_START,
            NUMBER_TAG_CACHE_END,
            DISABLE_RECIPE_AWARDING,
            OPTIMIZE_SIMPLE_MODEL
        );
        val path = FMLPaths.CONFIGDIR.get().resolve(String.format("%s-config.json", DataStructium.MOD_ID));
        try (val writer = Files.newBufferedWriter(path)) {
            DataStructium.GSON.toJson(snapshot, writer);
        } catch (IOException e) {
            DataStructium.LOGGER.error("unable to write config", e);
        }
    }
}
