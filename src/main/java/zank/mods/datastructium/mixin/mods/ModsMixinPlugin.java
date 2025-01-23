package zank.mods.datastructium.mixin.mods;

import lombok.val;
import net.minecraftforge.fml.ModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/**
 * @author ZZZank
 */
public class ModsMixinPlugin implements IMixinConfigPlugin {
    private String packageName;

    @Override
    public void onLoad(String mixinPackage) {
        packageName = mixinPackage;
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!mixinClassName.startsWith(packageName)) {
            return false;
        }
        mixinClassName = mixinClassName.substring(packageName.length() + 1); // +1 to remove the '.'
        val firstDot = mixinClassName.indexOf('.');
        if (firstDot < 0) {
            return false;
        }
        val modid = mixinClassName.substring(0, firstDot);
        return ModList.get().isLoaded(modid);
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
