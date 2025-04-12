package io.github.lumine1909.blocktuner.command.core;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import static io.github.lumine1909.blocktuner.BlockTunerPlugin.plugin;

public class CommandHandler {

    private static final Constructor<PluginCommand> CONSTRUCTOR_PluginCommand;

    static {
        try {
            CONSTRUCTOR_PluginCommand = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            CONSTRUCTOR_PluginCommand.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void registerCommands() {
        Reflections reflections = new Reflections("io.github.lumine1909.blocktuner.command");
        CommandMap commandMap = Bukkit.getCommandMap();

        for (Class<?> clazz : reflections.getTypesAnnotatedWith(RegisterCommand.class)) {
            if (!TabExecutor.class.isAssignableFrom(clazz)) {
                continue;
            }
            RegisterCommand annotation = clazz.getAnnotation(RegisterCommand.class);
            try {
                TabExecutor executor = (TabExecutor) clazz.getDeclaredConstructor().newInstance();
                PluginCommand command = createCommand(annotation.name());
                command.setExecutor(executor);
                command.setTabCompleter(executor);
                command.setAliases(Arrays.asList(annotation.aliases()));
                commandMap.register("paperblocktuner", command);
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to register command handler for: " + annotation.name());
                e.printStackTrace();
            }
        }
    }

    private static PluginCommand createCommand(String name) {
        try {
            return CONSTRUCTOR_PluginCommand.newInstance(name, plugin);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
