package zank.mods.datastructium.mixin.mods.ftbquests;

import dev.ftb.mods.ftbquests.util.FTBQuestsInventoryListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import zank.mods.datastructium.mods.ftbquests.QueuedFTBQuestInvListener;

@Mixin(FTBQuestsInventoryListener.class)
public abstract class MixinFTBQuestsInventoryListener {

    @Redirect(
        method = {"slotChanged", "refreshContainer"},
        at = @At(
            value = "INVOKE",
            target = "Ldev/ftb/mods/ftbquests/util/FTBQuestsInventoryListener;detect(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/ItemStack;J)V"
            // "why not remap=false", you might ask
            // Well, this is making mixin unable to find target, and I don't know why
        )
    )
    private void redirectInvDetect(ServerPlayer player, ItemStack craftedItem, long sourceTask) {
        QueuedFTBQuestInvListener.INSTANCE.addToQueue(player);
    }
}