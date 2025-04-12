package io.github.lumine1909.blocktuner.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;

public interface EditingGui extends InventoryHolder {

    void onClick(InventoryClickEvent event);

    default void onDrag(InventoryDragEvent event) {
        event.setCancelled(true);
    }

    default void onClose(InventoryCloseEvent event) {
    }
}