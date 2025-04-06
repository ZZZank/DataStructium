package zank.mods.datastructium.mixin.misc;

import net.minecraftforge.fml.loading.moddiscovery.Scanner;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author ZZZank
 */
@Mixin(value = Scanner.class, remap = false)
public class MixinScanner {

    @Redirect(method = "fileVisitor", at = @At(value = "INVOKE", target = "Lorg/objectweb/asm/ClassReader;accept(Lorg/objectweb/asm/ClassVisitor;I)V"))
    private void addScanFlags(ClassReader instance, ClassVisitor classVisitor, int parsingOptions) {
        instance.accept(classVisitor, parsingOptions | ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
    }
}
