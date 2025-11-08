package zank.mods.datastructium.mods.ftbquests;

import dev.ftb.mods.ftbquests.util.FTBQuestsInventoryListener;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import zank.mods.datastructium.DSConfig;

import java.util.function.Consumer;

/**
 * @author ZZZank
 */
public class QueuedFTBQuestInvListener implements Consumer<TickEvent.ServerTickEvent> {
    public static final QueuedFTBQuestInvListener INSTANCE = new QueuedFTBQuestInvListener();

    private int tickExisted = 0;

    private final ReferenceOpenHashSet<ServerPlayer> queue = new ReferenceOpenHashSet<>();

    public void addToQueue(ServerPlayer player) {
        queue.add(player);
    }

    @Override
    public void accept(TickEvent.ServerTickEvent event) {
        if (event.side.isClient()) {
            return;
        }

        tickExisted++;
        if (tickExisted == Integer.MAX_VALUE) {
            tickExisted = 0;
        }

        if (tickExisted % DSConfig.FTB_QUESTS_ITEM_CHECK_INTERVAL == 0) {
            for (var player : queue) {
                FTBQuestsInventoryListener.detect(player, ItemStack.EMPTY, 0);
            }
            queue.clear();
        }
    }
}
