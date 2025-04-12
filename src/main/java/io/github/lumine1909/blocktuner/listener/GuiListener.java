package io.github.lumine1909.blocktuner.listener;

import io.github.lumine1909.blocktuner.gui.EditingGui;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import static io.github.lumine1909.blocktuner.BlockTunerPlugin.plugin;

public class GuiListener implements Listener {

    public GuiListener() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof EditingGui gui) {
            gui.onClick(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof EditingGui gui) {
            gui.onClose(event);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof EditingGui gui) {
            gui.onDrag(event);
        }
    }
}
