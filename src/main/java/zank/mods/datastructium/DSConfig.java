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

    static {
        val snapshot = readFromFile();
        CANONICALIZE_QUADS = snapshot.canonicalizeQuads;
        CACHE_SHADER_UNIFORMS = snapshot.cacheShaderUniforms;
    }

    @AllArgsConstructor
    private static class Snapshot {
        @SerializedName("Canonicalize Quads")
        public final boolean canonicalizeQuads;
        @SerializedName("Cache Shader Uniforms")
        public final boolean cacheShaderUniforms;
        public Snapshot() {
            this(
                false,
                true
            );
        }
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
            CACHE_SHADER_UNIFORMS
        );
        val path = FMLPaths.CONFIGDIR.get().resolve(String.format("%s-config.json", DataStructium.MOD_ID));
        try (val writer = Files.newBufferedWriter(path)) {
            DataStructium.GSON.toJson(snapshot, writer);
        } catch (IOException e) {
            DataStructium.LOGGER.error("unable to write config", e);
        }
    }
}
