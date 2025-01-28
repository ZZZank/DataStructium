package zank.mods.datastructium;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DSConfig {
    public static final boolean CANONICALIZE_QUADS;
    public static final boolean CACHE_SHADER_UNIFORMS;
    public static final boolean TIERED_COMPOUND_TAG_INTERNAL;
    public static final int COMPOUND_TAG_RECONSTRUCT_THRESHOLD;

    static {
        val snapshot = readFromFile();
        CANONICALIZE_QUADS = snapshot.CANONICALIZE_QUADS;
        CACHE_SHADER_UNIFORMS = snapshot.CACHE_SHADER_UNIFORMS;
        COMPOUND_TAG_RECONSTRUCT_THRESHOLD = snapshot.COMPOUND_TAG_RECONSTRUCT_THRESHOLD;
        TIERED_COMPOUND_TAG_INTERNAL = snapshot.TIERED_COMPOUND_TAG_INTERNAL;
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
            COMPOUND_TAG_RECONSTRUCT_THRESHOLD
        );
        val path = FMLPaths.CONFIGDIR.get().resolve(String.format("%s-config.json", DataStructium.MOD_ID));
        try (val writer = Files.newBufferedWriter(path)) {
            DataStructium.GSON.toJson(snapshot, writer);
        } catch (IOException e) {
            DataStructium.LOGGER.error("unable to write config", e);
        }
    }
}
