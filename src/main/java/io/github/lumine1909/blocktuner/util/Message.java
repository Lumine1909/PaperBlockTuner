package io.github.lumine1909.blocktuner.util;

import com.google.common.collect.ImmutableList;
import io.github.lumine1909.blocktuner.object.Instrument;
import io.github.lumine1909.blocktuner.object.Note;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.lumine1909.blocktuner.BlockTunerPlugin.plugin;

public class Message {

    private static final Map<String, String> translations = new HashMap<>();

    public static List<String> SET_NOTE_SUGGESTIONS;
    public static List<String> SET_INSTRUMENT_SUGGESTIONS;

    public static void init() {
        plugin.reloadConfig();
        translations.clear();
        loadConfig();
        ImmutableList.Builder<String> noteBuilder = ImmutableList.builder();
        for (Note note : Note.values()) {
            noteBuilder.add("note=" + note.name().toLowerCase());
        }
        noteBuilder.add("<number>");
        SET_NOTE_SUGGESTIONS = noteBuilder.build();
        ImmutableList.Builder<String> instrumentBuilder = ImmutableList.builder();
        for (Instrument instrument : Instrument.values()) {
            instrumentBuilder.add("instrument=" + instrument.getName());
        }
        SET_INSTRUMENT_SUGGESTIONS = instrumentBuilder.build();
    }

    private static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    private static void loadConfig() {
        File transFile = new File(plugin.getDataFolder(), "translation.yml");
        if (transFile.isDirectory()) {
            transFile.delete();
        }
        if (!transFile.exists()) {
            plugin.saveResource("translation.yml", false);
        }
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(transFile);
        for (String key : cfg.getKeys(true)) {
            translations.put(key, color(cfg.getString(key)));
        }
    }

    public static String translate(String key) {
        return translations.get(key);
    }

    public static Component translatable(String key, Object... args) {
        String translated = String.format(translate(key), args);
        return MiniMessage.miniMessage().deserialize(translated).decoration(TextDecoration.ITALIC, false);
    }
}