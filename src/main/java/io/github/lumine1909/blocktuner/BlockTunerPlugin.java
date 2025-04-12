package io.github.lumine1909.blocktuner;

import io.github.lumine1909.blocktuner.data.PlayerData;
import io.github.lumine1909.blocktuner.display.InfoDisplayTask;
import io.github.lumine1909.blocktuner.gui.InstrumentTuneGui;
import io.github.lumine1909.blocktuner.gui.NoteTuneGui;
import io.github.lumine1909.blocktuner.gui.SettingsGui;
import io.github.lumine1909.blocktuner.listener.*;
import io.github.lumine1909.blocktuner.util.Message;
import io.github.lumine1909.blocktuner.util.StorageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;

import static io.github.lumine1909.blocktuner.command.core.CommandHandler.registerCommands;

public class BlockTunerPlugin extends JavaPlugin {

    /*TODO:
    1. Github ci
    2. Optimize tab complete
    */

    public static BlockTunerPlugin plugin;
    public static BukkitTask displayTask;
    public static String DATABASE_PATH;

    @Override
    public void onEnable() {
        plugin = this;
        DATABASE_PATH = new File(getDataFolder(), "playerdata.db").getAbsolutePath();
        callReload();
        registerEvents();
        registerCommands();
        for (Player player : Bukkit.getOnlinePlayers()) {
            NetworkListener.registerChannel(player);
            PlayerData.of(player);
        }
        displayTask = new InfoDisplayTask().runTaskTimer(this, 0, 2);
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
        StorageUtil.initDatabase();
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