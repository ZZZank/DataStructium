package zank.mods.datastructium.pools;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import zank.mods.datastructium.DSConfig;
import zank.mods.datastructium.DataStructium;

public final class ShaderCacheLoader {

    public static final Int2ObjectMap<ShaderProgramCache> SHADER_CACHE = new Int2ObjectOpenHashMap<>();

    public static void reload(String log) {
        RenderSystem.assertThread(RenderSystem::isOnRenderThread);
        if (!DSConfig.cacheShaderUniforms) {
            return;
        }

        DataStructium.LOGGER.info("Shader cache reload({})", log);
        SHADER_CACHE.clear();
    }

    public static int uniform(int i, CharSequence charSequence) {
        return SHADER_CACHE.computeIfAbsent(i, v -> new ShaderProgramCache(i))
            .uniform(charSequence);
    }
}