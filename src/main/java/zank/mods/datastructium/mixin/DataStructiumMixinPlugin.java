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
import java.util.function.BooleanSupplier;
import java.util.function.Function;

/**
 * @author ZZZank
 */
public class DataStructiumMixinPlugin implements IMixinConfigPlugin {

    public static final String MIXIN_PACKAGE_ROOT = DataStructiumMixinPlugin.class.getPackage().getName() + '.';
    public static final Logger LOGGER = LogManager.getLogger(DataStructiumMixinPlugin.class.getSimpleName());
    private static final Map<String, BooleanSupplier> OVERRIDES = new HashMap<>();
    private static final List<Function<String, BooleanSupplier>> OVERRIDE_FACTORIES = new ArrayList<>();

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
        override(
            "compound_tag_keys", () -> {
                var enable = DSConfig.DEDUPLICATE_COMPOUND_TAG_KEYS;
                if (DSConfig.COMPOUND_TAG_MODERNFIX && modPresent("modernfix")) {
                    LOGGER.warn(
                        "ModernFix installed, force disabling CompoundTag optimization. You can change this behaviour in './config/{}'",
                        DSConfig.CONFIG_FILE_NAME
                    );
                    enable = false;
                }
                return enable;
            }
        );
        constantOverride("vec3i_hashing", DSConfig.REPLACE_VEC3I_HASHING);
        constantOverride("fast_section_iterating", DSConfig.FAST_SECTION_ITERATING);
        constantOverride("disable_recipe_awarding", DSConfig.DISABLE_RECIPE_AWARDING);
        constantOverride("deduplicate_ingredient", DSConfig.DEDUPLICATE_INGREDIENT);
    }

    private static boolean modPresent(String modId) {
        return FMLLoader.getLoadingModList().getModFileById(modId) != null;
    }

    private static void constantOverride(String key, boolean value) {
        override(key, () -> value);
    }

    private static void override(String key, BooleanSupplier value) {
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
        if (DSConfig.DISABLE_ALL_MIXINS) {
            return false;
        }

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
            LOGGER.warn("Mixin class '{}' not categorized, using default behaviour (approve)", dotMixinClass);
            // not categorized
            return true;
        }

        val key = trimmed.substring(0, lastIndex);
        var got = OVERRIDES.get(key);
        if (got == null) {
            got = OVERRIDE_FACTORIES.stream()
                .map(f -> f.apply(key))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
            if (got != null) {
                override(key, got);
            }
        }

        if (got == null) {
            LOGGER.info("No override for mixin class '{}', using default behaviour (approve)", dotMixinClass);
            // no override
            return true;
        }

        val approved = got.getAsBoolean();
        LOGGER.info("Mixin class '{}' is {} by override", dotMixinClass, approved ? "approved" : "rejected");
        return approved;
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
