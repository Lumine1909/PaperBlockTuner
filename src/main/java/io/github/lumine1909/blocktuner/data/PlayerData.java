package io.github.lumine1909.blocktuner.data;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.lumine1909.blocktuner.gui.InstrumentTuneGui;
import io.github.lumine1909.blocktuner.gui.NoteTuneGui;
import io.github.lumine1909.blocktuner.gui.SettingsGui;
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
import java.util.UUID;

public class PlayerData {

    public static final Cache<Player, PlayerData> PLAYER_DATA_CACHE = CacheBuilder.newBuilder().build();

    public Player player;
    public UUID uuid;

    public boolean enableStickNoteTuning = true;
    public boolean enableStickInstrumentTuning = true;
    public boolean enableItemScrollTuning = true;
    public boolean enableBlockScrollTuning = true;
    public boolean syncBlockInstrument = false;
    public boolean isItemScrollTuning = false;
    public boolean isBlockScrollTuning = false;

    public Block currentBlock = null;
    public NoteBlock currentNoteBlock = null;

    public PlayerData(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
    }

    public static PlayerData of(Player player) {
        if (PLAYER_DATA_CACHE.getIfPresent(player) == null) {
            PLAYER_DATA_CACHE.put(player, StorageUtil.loadOrCreate(player));
        }
        return PLAYER_DATA_CACHE.getIfPresent(player);
    }

    public static void delete(Player player) {
        PlayerData data = PLAYER_DATA_CACHE.getIfPresent(player);
        if (data != null) {
            StorageUtil.save(data);
        }
        PLAYER_DATA_CACHE.invalidate(player);
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
        String sql = "REPLACE INTO playerdata (uuid, enable_stick_note, enable_stick_instrument, enable_item_scroll, enable_block_scroll, sync_instrument) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.setBoolean(2, enableStickNoteTuning);
            stmt.setBoolean(3, enableStickInstrumentTuning);
            stmt.setBoolean(4, enableItemScrollTuning);
            stmt.setBoolean(5, enableBlockScrollTuning);
            stmt.setBoolean(6, syncBlockInstrument);
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
            }
        }
    }
}