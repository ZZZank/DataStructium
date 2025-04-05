package zank.mods.datastructium.mixin;

import lombok.val;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import zank.mods.datastructium.DSConfig;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author ZZZank
 */
public class DataStructiumMixinPlugin implements IMixinConfigPlugin {

    public static final String MIXIN_PACKAGE_ROOT = DataStructiumMixinPlugin.class.getPackage().getName() + '.';
    public static final Logger LOGGER = LogManager.getLogger(DataStructiumMixinPlugin.class.getSimpleName());
    private static final Map<String, Supplier<Boolean>> OVERRIDES = new HashMap<>();
    private static final List<Function<String, Supplier<Boolean>>> OVERRIDE_FACTORIES = new ArrayList<>();

    static {
        OVERRIDE_FACTORIES.add(key -> {
            val parts = key.split("\\.");
            return parts.length > 1 && "mods".equals(parts[0])
                ? () -> modPresent(parts[1])
                : null;
        });
        constantOverride("cache_number_tag", DSConfig.CACHE_NUMBER_TAG);
        constantOverride("optimize_small_model", DSConfig.OPTIMIZE_SIMPLE_MODEL);
        constantOverride("cache_shader_uniform", DSConfig.CACHE_SHADER_UNIFORMS);
        constantOverride("canonicalize_quads", DSConfig.CANONICALIZE_QUADS);
        constantOverride("compound_tag_internal", DSConfig.TIERED_COMPOUND_TAG_INTERNAL);
        constantOverride("block_pos_hashing", DSConfig.REPLACE_BLOCK_POS_HASHING);
    }

    private static boolean modPresent(String modId) {
        return FMLLoader.getLoadingModList().getModFileById(modId) != null;
    }

    private static void constantOverride(String key, Boolean value) {
        OVERRIDES.put(key, () -> value);
    }

    private static void override(String key, Supplier<Boolean> value) {
        OVERRIDES.put(key, value);
    }

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        val dotMixinClass = mixinClassName.replace('/', '.');
        if (!dotMixinClass.startsWith(MIXIN_PACKAGE_ROOT)) {
            LOGGER.error(
                "Expected mixin '{}' to start with package root '{}', treating as foreign and disabling!",
                dotMixinClass,
                MIXIN_PACKAGE_ROOT
            );
            return false;
        }

        val trimmed = dotMixinClass.substring(MIXIN_PACKAGE_ROOT.length());

        val lastIndex = trimmed.lastIndexOf('.');
        if (lastIndex == -1) {
            // not categorized
            return true;
        }

        val key = trimmed.substring(0, lastIndex);
        val override = OVERRIDES.computeIfAbsent(
            key, k -> OVERRIDE_FACTORIES.stream()
                .map(f -> f.apply(k))
                .findFirst()
                .orElse(null)
        );
        if (override == null) {
            // no override
            return true;
        }

        return override.get();
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
