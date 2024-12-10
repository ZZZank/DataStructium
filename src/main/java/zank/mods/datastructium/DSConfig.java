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
    public static final boolean CANONICALIZE_RESOURCE_LOCATION;
    public static final boolean CANONICALIZE_MODEL_RESOURCE_LOCATION;
    public static final boolean CANONICALIZE_RESOURCE_KEY;

    static {
        val snapshot = readFromFile();
        CANONICALIZE_QUADS = snapshot.canonicalizeQuads;
        CACHE_SHADER_UNIFORMS = snapshot.cacheShaderUniforms;
        CANONICALIZE_RESOURCE_LOCATION = snapshot.canonicalizeResourceLocation;
        CANONICALIZE_MODEL_RESOURCE_LOCATION = snapshot.canonicalizeModelResourceLocation;
        CANONICALIZE_RESOURCE_KEY = snapshot.canonicalizeResourceKey;
    }

    @AllArgsConstructor
    private static class Snapshot {
        @SerializedName("Canonicalize Quads")
        public final boolean canonicalizeQuads;
        @SerializedName("Cache Shader Uniforms")
        public final boolean cacheShaderUniforms;
        @SerializedName("Canonicalize ResourceLocation")
        public final boolean canonicalizeResourceLocation;
        @SerializedName("Canonicalize ModelResourceLocation")
        public final boolean canonicalizeModelResourceLocation;
        @SerializedName("Canonicalize ResourceKey")
        public final boolean canonicalizeResourceKey;

        public Snapshot() {
            this(false, true, false, false, false);
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
            CACHE_SHADER_UNIFORMS,
            CANONICALIZE_RESOURCE_LOCATION,
            CANONICALIZE_MODEL_RESOURCE_LOCATION,
            CANONICALIZE_RESOURCE_KEY
        );
        val path = FMLPaths.CONFIGDIR.get().resolve(String.format("%s-config.json", DataStructium.MOD_ID));
        try (val writer = Files.newBufferedWriter(path)) {
            DataStructium.GSON.toJson(snapshot, writer);
        } catch (IOException e) {
            DataStructium.LOGGER.error("unable to write config", e);
        }
    }
}
