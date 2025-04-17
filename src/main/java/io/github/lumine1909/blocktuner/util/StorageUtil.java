package io.github.lumine1909.blocktuner.util;

import io.github.lumine1909.blocktuner.data.PlayerData;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static io.github.lumine1909.blocktuner.BlockTunerPlugin.DATABASE_PATH;

public class StorageUtil {

    private static final String SQL_CREATE = """
        CREATE TABLE IF NOT EXISTS playerdata (
            uuid TEXT PRIMARY KEY,
            enable_stick_note BOOLEAN,
            enable_stick_instrument BOOLEAN,
            enable_item_scroll BOOLEAN,
            enable_block_scroll BOOLEAN,
            sync_instrument BOOLEAN
        );
        """;

    public static PlayerData loadOrCreate(Player player) {
        PlayerData data = new PlayerData(player);
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH)) {
            data.loadFromDatabase(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void save(PlayerData data) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH)) {
            data.saveToDatabase(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH)) {
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(SQL_CREATE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}