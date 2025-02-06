package zank.mods.datastructium;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;

/**
 * @author ZZZank
 */
@UtilityClass
public final class DSConfig {
    public final boolean CANONICALIZE_QUADS;
    public final boolean CACHE_SHADER_UNIFORMS;
    public final boolean TIERED_COMPOUND_TAG_INTERNAL;
    public final int COMPOUND_TAG_RECONSTRUCT_THRESHOLD;
    public final boolean ENABLE_NUMBER_TAG_CACHE;
    public final int NUMBER_TAG_CACHE_START;
    public final int NUMBER_TAG_CACHE_END;

    static {
        val snapshot = readFromFile();
        CANONICALIZE_QUADS = snapshot.CANONICALIZE_QUADS;
        CACHE_SHADER_UNIFORMS = snapshot.CACHE_SHADER_UNIFORMS;
        COMPOUND_TAG_RECONSTRUCT_THRESHOLD = snapshot.COMPOUND_TAG_RECONSTRUCT_THRESHOLD;
        TIERED_COMPOUND_TAG_INTERNAL = snapshot.TIERED_COMPOUND_TAG_INTERNAL;
        NUMBER_TAG_CACHE_START = snapshot.NUMBER_TAG_CACHE_START;
        NUMBER_TAG_CACHE_END = snapshot.NUMBER_TAG_CACHE_END;
        ENABLE_NUMBER_TAG_CACHE = snapshot.ENABLE_NUMBER_TAG_CACHE;
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
        @SerializedName("Enable Number Tag Cache")
        public boolean ENABLE_NUMBER_TAG_CACHE = true;
        @SerializedName("Number Tag Cache starts at (inclusive)")
        public int NUMBER_TAG_CACHE_START = -4096;
        @SerializedName("Number Tag Cache ends at (exclusive)")
        public int NUMBER_TAG_CACHE_END = 4096;
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
            ENABLE_NUMBER_TAG_CACHE,
            NUMBER_TAG_CACHE_START,
            NUMBER_TAG_CACHE_END
        );
        val path = FMLPaths.CONFIGDIR.get().resolve(String.format("%s-config.json", DataStructium.MOD_ID));
        try (val writer = Files.newBufferedWriter(path)) {
            DataStructium.GSON.toJson(snapshot, writer);
        } catch (IOException e) {
            DataStructium.LOGGER.error("unable to write config", e);
        }
    }
}
