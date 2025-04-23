package io.github.lumine1909.blocktuner;

import io.github.lumine1909.blocktuner.data.PlayerData;
import io.github.lumine1909.blocktuner.display.InfoDisplayTask;
import io.github.lumine1909.blocktuner.gui.InstrumentTuneGui;
import io.github.lumine1909.blocktuner.gui.NoteTuneGui;
import io.github.lumine1909.blocktuner.gui.SettingsGui;
import io.github.lumine1909.blocktuner.listener.*;
import io.github.lumine1909.blocktuner.metrics.Metrics;
import io.github.lumine1909.blocktuner.util.Message;
import io.github.lumine1909.blocktuner.util.StorageUtil;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static io.github.lumine1909.blocktuner.command.core.CommandHandler.registerCommands;

public class BlockTunerPlugin extends JavaPlugin {

    public static BlockTunerPlugin plugin;
    public static ScheduledTask displayTask;
    public static String DATABASE_PATH;

    @Override
    public void onEnable() {
        plugin = this;
        DATABASE_PATH = new File(getDataFolder(), "playerdata.db").getAbsolutePath();
        callReload();
        StorageUtil.initDatabase();
        registerEvents();
        registerCommands();
        for (Player player : Bukkit.getOnlinePlayers()) {
            NetworkListener.registerChannel(player);
            PlayerData.of(player);
        }
        displayTask = Bukkit.getGlobalRegionScheduler().runAtFixedRate(this, (task) -> new InfoDisplayTask().run(), 1, 2);
        //displayTask = new InfoDisplayTask().runTaskTimer(this, 0, 2);
        new Metrics(this, 25453);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            NetworkListener.unregisterChannel(player);
            StorageUtil.save(PlayerData.of(player));
        }
        if (displayTask != null) {
            displayTask.cancel();
        }
    }

    public void callReload() {
        Message.init();
        NoteTuneGui.init();
        InstrumentTuneGui.init();
        SettingsGui.init();
    }

    private void registerEvents() {
        new NetworkListener();
        new NotePlayListener();
        new DisplayTaskListener();
        new DataHandleListener();
        new StickInteractListener();
        new ScrollTuningListener();
        new GuiListener();
    }
}