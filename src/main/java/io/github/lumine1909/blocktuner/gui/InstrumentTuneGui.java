package io.github.lumine1909.blocktuner.gui;

import io.github.lumine1909.blocktuner.data.PlayerData;
import io.github.lumine1909.blocktuner.object.Instrument;
import io.github.lumine1909.blocktuner.object.Note;
import io.github.lumine1909.blocktuner.util.ItemBuilder;
import io.github.lumine1909.blocktuner.util.Message;
import io.github.lumine1909.blocktuner.util.TuneUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class InstrumentTuneGui implements EditingGui {

    public static Inventory INVENTORY;

    public static void init() {
        INVENTORY = Bukkit.createInventory(new InstrumentTuneGui(), 27, Message.translatable("gui-name.instrument"));
        int i = 0;
        for (Instrument instrument : Instrument.values()) {
            if (instrument == Instrument.EMPTY) {
                continue;
            }
            INVENTORY.setItem(i, ItemBuilder.of(instrument.getMaterial()).name(Message.translatable("instrument." + instrument.getName())).build());
            i++;
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return INVENTORY;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        int slot = event.getRawSlot();
        if (slot < 0 || slot >= Instrument.values().length - 1) {
            return;
        }
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        PlayerData data = PlayerData.of(player);
        if (data.currentBlock != null && data.currentNoteBlock != null) {
            player.closeInventory();
            TuneUtil.tune(player, data.currentBlock, Note.EMPTY, Instrument.values()[slot]);
            data.currentBlock = null;
            data.currentNoteBlock = null;
        }
    }
}
