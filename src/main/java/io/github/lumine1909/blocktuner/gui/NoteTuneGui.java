package io.github.lumine1909.blocktuner.gui;

import io.github.lumine1909.blocktuner.data.PlayerData;
import io.github.lumine1909.blocktuner.object.Instrument;
import io.github.lumine1909.blocktuner.util.ItemBuilder;
import io.github.lumine1909.blocktuner.util.Message;
import io.github.lumine1909.blocktuner.util.NoteUtil;
import io.github.lumine1909.blocktuner.util.TuneUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class NoteTuneGui implements EditingGui {

    public static Inventory INVENTORY;

    public static void init() {
        INVENTORY = Bukkit.createInventory(new NoteTuneGui(), 27, Message.translatable("gui-name.note"));
        for (int i = 0; i < 25; i++) {
            INVENTORY.setItem(i, ItemBuilder.of(Material.NOTE_BLOCK, i).name(Message.translatable("item.noteblock-name", Instrument.EMPTY, NoteUtil.byNote(i))).build());
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return INVENTORY;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 24) {
            return;
        }
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        PlayerData data = PlayerData.of(player);
        if (data.currentBlock != null && data.currentNoteBlock != null) {
            TuneUtil.tune(player, data.currentBlock, NoteUtil.byNote(slot), Instrument.EMPTY);
            player.closeInventory();
            data.currentBlock = null;
            data.currentNoteBlock = null;
        }
    }
}
