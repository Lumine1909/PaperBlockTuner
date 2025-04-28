package io.github.lumine1909.blocktuner.listener;

import io.github.lumine1909.blocktuner.data.PlayerData;
import io.github.lumine1909.blocktuner.object.Instrument;
import io.github.lumine1909.blocktuner.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import static io.github.lumine1909.blocktuner.BlockTunerPlugin.plugin;

public class ScrollTuningListener implements Listener {

    public ScrollTuningListener() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onItemSwap(PlayerSwapHandItemsEvent event) {
        PlayerData data = PlayerData.of(event.getPlayer());
        if (event.getOffHandItem().getType() == Material.NOTE_BLOCK && data.enableItemScrollTuning) {
            boolean bl = !data.isItemScrollTuning;
            PlayerData.of(event.getPlayer()).isItemScrollTuning = bl;
            event.getPlayer().sendMessage(bl ? Message.translatable("notice.scroll-item-activate") : Message.translatable("notice.scroll-item-deactivate"));
            event.setCancelled(true);
        }
        if (TuneStickUtil.isTuneStick(event.getOffHandItem()) && data.enableBlockScrollTuning) {
            boolean bl = !data.isBlockScrollTuning;
            PlayerData.of(event.getPlayer()).isBlockScrollTuning = bl;
            event.getPlayer().sendMessage(bl ? Message.translatable("notice.scroll-block-activate") : Message.translatable("notice.scroll-block-deactivate"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onScrollItem(PlayerItemHeldEvent event) {
        if (!PlayerData.of(event.getPlayer()).isItemScrollTuning || event.getPlayer().getInventory().getItem(event.getPreviousSlot()).getType() != Material.NOTE_BLOCK) {
            return;
        }
        ItemStack noteBlock = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
        int next = Math.floorMod(ItemUtil.getNote(noteBlock).getNote() + calcDiff(event.getPreviousSlot(), event.getNewSlot()), 25);
        event.getPlayer().getInventory().setItemInMainHand(ItemUtil.setNote(noteBlock, NoteUtil.byNote(next)));
        event.setCancelled(true);
    }

    @EventHandler
    public void onScrollBlock(PlayerItemHeldEvent event) {
        if (!PlayerData.of(event.getPlayer()).isBlockScrollTuning || !TuneStickUtil.isTuneStick(event.getPlayer().getInventory().getItem(event.getPreviousSlot()))) {
            return;
        }
        Block block = event.getPlayer().getTargetBlock(null, 5);
        if (!(block.getBlockData() instanceof NoteBlock noteBlock) || !EditPermissionUtil.canEdit(event.getPlayer(), block.getLocation())) {
            return;
        }
        int next = Math.floorMod(noteBlock.getNote().getId() + calcDiff(event.getPreviousSlot(), event.getNewSlot()), 25);
        TuneUtil.tune(event.getPlayer(), block, NoteUtil.byNote(next), Instrument.EMPTY);
        event.setCancelled(true);
    }

    private static int calcDiff(int prev, int next) {
        if (prev - next > 4) {
            return prev - next - 9;
        }
        if (next - prev > 4) {
            return prev - next + 9;
        }
        return prev - next;
    }
}
