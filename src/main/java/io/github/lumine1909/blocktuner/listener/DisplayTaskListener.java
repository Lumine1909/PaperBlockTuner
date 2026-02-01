package io.github.lumine1909.blocktuner.listener;

import io.github.lumine1909.blocktuner.display.InfoDisplayTask;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static io.github.lumine1909.blocktuner.BlockTunerPlugin.plugin;

public class DisplayTaskListener implements Listener {

    public DisplayTaskListener() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        InfoDisplayTask.cacheTrackMap.remove(event.getPlayer().getUniqueId());
    }
}