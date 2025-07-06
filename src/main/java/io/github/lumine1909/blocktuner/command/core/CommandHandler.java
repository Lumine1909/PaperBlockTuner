package io.github.lumine1909.blocktuner.command.core;

import io.github.lumine1909.blocktuner.BlockTunerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static io.github.lumine1909.blocktuner.BlockTunerPlugin.plugin;

public class CommandHandler {

    private static final Constructor<PluginCommand> constructor$PluginCommand;
    private static final CommandMap COMMAND_MAP = Bukkit.getCommandMap();

    static {
        try {
            constructor$PluginCommand = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor$PluginCommand.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void registerCommands() {
        URL jarUrl = BlockTunerPlugin.class.getProtectionDomain().getCodeSource().getLocation();
        try (JarFile jarFile = new JarFile(new File(jarUrl.toURI()))) {
            for (Iterator<JarEntry> it = jarFile.entries().asIterator(); it.hasNext(); ) {
                JarEntry entry = it.next();
                String entryName = entry.getName();
                if (!entryName.startsWith("io/github/lumine1909/blocktuner") || !entryName.endsWith(".class")) {
                    continue;
                }
                String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);
                registerCommand(Class.forName(className));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void registerCommand(Class<?> clazz) {
        if (!TabExecutor.class.isAssignableFrom(clazz) || !clazz.isAnnotationPresent(RegisterCommand.class)) {
            return;
        }
        RegisterCommand annotation = clazz.getAnnotation(RegisterCommand.class);
        try {
            TabExecutor executor = (TabExecutor) clazz.getDeclaredConstructor().newInstance();
            PluginCommand command = createCommand(annotation.name());
            command.setExecutor(executor);
            command.setTabCompleter(executor);
            command.setAliases(Arrays.asList(annotation.aliases()));
            COMMAND_MAP.register("paperblocktuner", command);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to register command handler for: " + annotation.name());
            e.printStackTrace();
        }
    }

    private static PluginCommand createCommand(String name) {
        try {
            return constructor$PluginCommand.newInstance(name, plugin);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}