package io.github.lumine1909.blocktuner.listener;

import io.github.lumine1909.blocktuner.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static io.github.lumine1909.blocktuner.BlockTunerPlugin.plugin;

public class DataHandleListener implements Listener {

    public DataHandleListener() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerData.of(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerData.delete(event.getPlayer());
    }
}
