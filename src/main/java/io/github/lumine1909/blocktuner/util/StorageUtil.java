package io.github.lumine1909.blocktuner.util;

import io.github.lumine1909.blocktuner.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;

import static io.github.lumine1909.blocktuner.BlockTunerPlugin.DATABASE_PATH;
import static io.github.lumine1909.blocktuner.BlockTunerPlugin.plugin;

public class StorageUtil {

    private static final String SQL_CREATE = """
        CREATE TABLE IF NOT EXISTS playerdata (
            uuid TEXT PRIMARY KEY,
            enable_stick_note BOOLEAN,
            enable_stick_instrument BOOLEAN,
            enable_item_scroll BOOLEAN,
            enable_block_scroll BOOLEAN,
            sync_instrument BOOLEAN,
            copy_instrument BOOLEAN
        );
        """;

    private static final String SQL_ADD_COPY_INSTRUMENT =
        "ALTER TABLE playerdata ADD COLUMN copy_instrument BOOLEAN NOT NULL DEFAULT 0";

    public static void loadOrCreate(Player player, Consumer<PlayerData> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            syncLoadOrCreate(player, consumer);
        });
    }

    public static void syncLoadOrCreate(Player player, Consumer<PlayerData> consumer) {
        PlayerData data = new PlayerData(player);
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH)) {
            data.loadFromDatabase(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        consumer.accept(data);
    }

    public static void save(PlayerData data) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            syncSave(data);
        });
    }

    public static void syncSave(PlayerData data) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH)) {
            data.saveToDatabase(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(SQL_CREATE);
            migrateDatabase(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void migrateDatabase(Statement statement) throws SQLException {
        try {
            statement.executeUpdate(SQL_ADD_COPY_INSTRUMENT);
        } catch (SQLException e) {
            if (!e.getMessage().contains("duplicate column name")) {
                throw e;
            }
        }
    }
}
