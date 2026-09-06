package io.github.lumine1909.blocktuner.data;

import io.github.lumine1909.blocktuner.gui.InstrumentTuneGui;
import io.github.lumine1909.blocktuner.gui.NoteTuneGui;
import io.github.lumine1909.blocktuner.gui.SettingsGui;
import io.github.lumine1909.blocktuner.util.Message;
import io.github.lumine1909.blocktuner.util.StorageUtil;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerData {

    public static final Map<Player, PlayerData> PLAYER_DATA_CACHE = new ConcurrentHashMap<>();

    public final Player player;
    public final UUID uuid;

    public boolean enableStickNoteTuning = true;
    public boolean enableStickInstrumentTuning = true;
    public boolean enableItemScrollTuning = true;
    public boolean enableBlockScrollTuning = true;
    public boolean syncBlockInstrument = false;
    public boolean copyInstrument = false;
    public boolean isItemScrollTuning = false;
    public boolean isBlockScrollTuning = false;

    public Block currentBlock = null;
    public NoteBlock currentNoteBlock = null;

    public PlayerData(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
    }

    public static PlayerData of(Player player) {
        if (!PLAYER_DATA_CACHE.containsKey(player)) {
            player.sendMessage(Message.translatable("error.data-not-load"));
        }
        return PLAYER_DATA_CACHE.get(player);
    }

    public static void create(Player player, boolean async) {
        if (async) {
            StorageUtil.loadOrCreate(player, data -> PLAYER_DATA_CACHE.put(player, data));
        } else {
            StorageUtil.syncLoadOrCreate(player, data -> PLAYER_DATA_CACHE.put(player, data));
        }
    }

    public static void delete(Player player) {
        PlayerData data = PLAYER_DATA_CACHE.get(player);
        if (data != null) {
            StorageUtil.save(data);
        }
        PLAYER_DATA_CACHE.remove(player);
    }

    public void startEdit(Block block, NoteBlock noteBlock, PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && enableStickNoteTuning) {
            currentBlock = block;
            currentNoteBlock = noteBlock;
            event.getPlayer().openInventory(NoteTuneGui.INVENTORY);
        }
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && enableStickInstrumentTuning) {
            currentBlock = block;
            currentNoteBlock = noteBlock;
            event.getPlayer().openInventory(InstrumentTuneGui.INVENTORY);
        }
        event.setCancelled(true);
    }

    public void toggleSetting() {
        player.openInventory(new SettingsGui(this).getInventory());
    }

    public void saveToDatabase(Connection connection) throws SQLException {
        String sql = "REPLACE INTO playerdata (uuid, enable_stick_note, enable_stick_instrument, enable_item_scroll, enable_block_scroll, sync_instrument, copy_instrument) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.setBoolean(2, enableStickNoteTuning);
            stmt.setBoolean(3, enableStickInstrumentTuning);
            stmt.setBoolean(4, enableItemScrollTuning);
            stmt.setBoolean(5, enableBlockScrollTuning);
            stmt.setBoolean(6, syncBlockInstrument);
            stmt.setBoolean(7, copyInstrument);
            stmt.executeUpdate();
        }
    }

    public void loadFromDatabase(Connection connection) throws SQLException {
        String sql = "SELECT * FROM playerdata WHERE uuid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                enableStickNoteTuning = rs.getBoolean("enable_stick_note");
                enableStickInstrumentTuning = rs.getBoolean("enable_stick_instrument");
                enableItemScrollTuning = rs.getBoolean("enable_item_scroll");
                enableBlockScrollTuning = rs.getBoolean("enable_block_scroll");
                syncBlockInstrument = rs.getBoolean("sync_instrument");
                copyInstrument = rs.getBoolean("copy_instrument");
            }
        }
    }
}
