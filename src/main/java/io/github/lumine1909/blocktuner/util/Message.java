package io.github.lumine1909.blocktuner.util;

import com.google.common.collect.ImmutableList;
import io.github.lumine1909.blocktuner.object.Instrument;
import io.github.lumine1909.blocktuner.object.Note;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.lumine1909.blocktuner.BlockTunerPlugin.plugin;

public class Message {

    private static final Map<String, String> TRANSLATIONS = new HashMap<>();
    public static List<String> SET_NOTE_SUGGESTIONS;
    public static List<String> SET_INSTRUMENT_SUGGESTIONS;

    public static void init() {
        plugin.reloadConfig();
        TRANSLATIONS.clear();
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

    private static void loadConfig() {
        plugin.saveDefaultConfig();
        File transFile = new File(plugin.getDataFolder(), "translation.yml");
        if (transFile.isDirectory()) {
            transFile.delete();
        }
        if (!transFile.exists()) {
            plugin.saveResource("translation.yml", false);
        }

        FileConfiguration cfg = YamlConfiguration.loadConfiguration(transFile);

        Language language = Language.getLanguage(plugin.getConfig().getString("built-in-language"));
        if (language == Language.NONE) {
            InputStream fallbackStream = plugin.getResource("translation_" + Language.ENGLISH.getName() + ".yml");
            if (fallbackStream == null) {
                throw new RuntimeException("Failed to load language file: " + language.getName());
            }
            handleTranslationFile(transFile, YamlConfiguration.loadConfiguration(new InputStreamReader(fallbackStream)));
        } else {
            InputStream inputStream = plugin.getResource("translation_" + language.getName() + ".yml");
            if (inputStream == null) {
                throw new RuntimeException("Failed to load language file: " + language.getName());
            }
            handleTranslationFile(null, YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream)));
        }
    }

    private static void handleTranslationFile(File file, FileConfiguration fallback) {
        if (file == null) {
            for (String key : fallback.getKeys(true)) {
                TRANSLATIONS.put(key, fallback.getString(key));
            }
            return;
        }
        FileConfiguration translation = YamlConfiguration.loadConfiguration(file);
        for (String key : fallback.getKeys(true)) {
            if (!translation.contains(key)) {
                translation.set(key, fallback.getString(key));
            }
        }
        for (String key : translation.getKeys(true)) {
            String translate = translation.getString(key);
            if (!fallback.contains(key)) {
                translation.set(key, null);
            } else if (translate != null) {
                TRANSLATIONS.put(key, translate);
            }
        }
        try {
            translation.save(file);
        } catch (IOException ignored) {
        }
    }

    public static String translate(String key) {
        return TRANSLATIONS.get(key);
    }

    public static Component translatable(String key, Object... args) {
        String translated = translate(key).formatted(args);
        return MiniMessage.miniMessage().deserialize(translated).decoration(TextDecoration.ITALIC, false);
    }

    enum Language {
        CHINESE("zh_CN"),
        ENGLISH("en_US"),
        NONE("");

        public static final Map<String, Language> NAME_LOOKUP = new HashMap<>();

        static {
            for (Language language : Language.values()) {
                NAME_LOOKUP.put(language.name, language);
            }
        }

        private final String name;

        Language(String name) {
            this.name = name;
        }

        public static Language getLanguage(String name) {
            return NAME_LOOKUP.getOrDefault(name, NONE);
        }

        public String getName() {
            return name;
        }
    }
}