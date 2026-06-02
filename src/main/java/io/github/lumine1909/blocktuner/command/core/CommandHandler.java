package io.github.lumine1909.blocktuner.command.core;

import io.github.lumine1909.blocktuner.BlockTunerPlugin;
import io.github.lumine1909.reflexion.Method;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static io.github.lumine1909.blocktuner.BlockTunerPlugin.plugin;

public class CommandHandler {

    private static final Method<PluginCommand> constructor$PluginCommand = Method.of(PluginCommand.class, "<init>", PluginCommand.class, String.class, Plugin.class);
    private static final CommandMap COMMAND_MAP = Bukkit.getCommandMap();


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
            PluginCommand command = constructor$PluginCommand.invoke(null, annotation.name(), plugin);
            command.setExecutor(executor);
            command.setTabCompleter(executor);
            command.setAliases(Arrays.asList(annotation.aliases()));
            //command.setPermission(annotation.permission()); We need apply I18N to the permission message.
            COMMAND_MAP.register("paperblocktuner", command);
        } catch (Exception e) {
            plugin.getSLF4JLogger().error("Failed to register command handler for: {}", annotation.name(), e);
        }
    }
}