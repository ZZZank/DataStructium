package zank.mods.datastructium.mixin.mods.kubejs;

import com.google.common.base.Joiner;
import dev.latvian.kubejs.KubeJSPaths;
import dev.latvian.kubejs.script.data.KubeJSResourcePack;
import dev.latvian.kubejs.util.UtilsJS;
import lombok.val;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import zank.mods.datastructium.DataStructium;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author ZZZank
 */
@Mixin(value = KubeJSResourcePack.class, remap = false)
public class MixinKubeJSResourcePack {

    @Unique
    private static final Joiner PATH_JOINER = Joiner.on('/');
    @Unique
    private List<Path> dataStruct$cachedPaths = null;

    @Redirect(
        method = "getResources",
        at = @At(
            value = "INVOKE",
            target = "Ldev/latvian/kubejs/util/UtilsJS;tryIO(Ldev/latvian/kubejs/util/UtilsJS$TryIO;)V"
        ),
        require = 0 // KesseractJS removed 'tryIO' call and don't need our optimization
    )
    private void skipOriginalFileWalking(UtilsJS.TryIO ex) {
        // do nothing
    }

    @Inject(
        method = "getResources",
        at = @At(
            value = "INVOKE",
            target = "Ldev/latvian/kubejs/util/UtilsJS;tryIO(Ldev/latvian/kubejs/util/UtilsJS$TryIO;)V"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        require = 0 // KesseractJS removed 'tryIO' call and don't need our optimization
    )
    private void doCachedFileWalking(
        PackType type,
        String namespace,
        String path,
        int maxDepth,
        Predicate<String> filter,
        CallbackInfoReturnable<Collection<ResourceLocation>> cir,
        List<ResourceLocation> list
    ) {
        val root = KubeJSPaths.get(type).toAbsolutePath();
        if (dataStruct$cachedPaths == null) {
            if (Files.exists(root) && Files.isDirectory(root)) {
                try {
                    dataStruct$cachedPaths = Files.walk(root)
                        .map(Path::toAbsolutePath)
                        .map(root::relativize)
                        .filter(p -> !p.toString().endsWith(".mcmeta"))
                        .collect(Collectors.toList());
                } catch (IOException ex) {
                    DataStructium.LOGGER.error("Error when initializing KubeJS resource cache", ex);
                    dataStruct$cachedPaths = Collections.emptyList();
                }
            } else {
                dataStruct$cachedPaths = Collections.emptyList();
            }
        }
        val inputPath = root.getFileSystem().getPath(path);
        dataStruct$cachedPaths
            .stream()
            .filter(p -> p.getNameCount() > 1 && p.getNameCount() - 1 <= maxDepth)
            .filter(p -> p.subpath(1, p.getNameCount()).startsWith(inputPath))
            .filter(p -> filter.test(p.getFileName().toString()))
            .map(p -> new ResourceLocation(
                p.getName(0).toString(),
                PATH_JOINER.join(p.subpath(1, Math.min(maxDepth, p.getNameCount())))
            ))
            .forEach(list::add);
    }
}
