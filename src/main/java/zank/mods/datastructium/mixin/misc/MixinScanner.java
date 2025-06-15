package zank.mods.datastructium.mixin.misc;

import net.minecraftforge.fml.loading.moddiscovery.Scanner;
import org.objectweb.asm.ClassReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * @author ZZZank
 */
@Mixin(value = Scanner.class, remap = false)
public class MixinScanner {

    @ModifyArg(
        method = "fileVisitor",
        at = @At(value = "INVOKE", target = "Lorg/objectweb/asm/ClassReader;accept(Lorg/objectweb/asm/ClassVisitor;I)V"),
        index = 2
    )
    private static int appendScanFlags(int parsingOptions) {
        return parsingOptions | ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
    }
}
