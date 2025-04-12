package io.github.lumine1909.blocktuner.command;

import io.github.lumine1909.blocktuner.command.core.RegisterCommand;
import io.github.lumine1909.blocktuner.data.PlayerData;
import io.github.lumine1909.blocktuner.util.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.lumine1909.blocktuner.BlockTunerPlugin.plugin;

@RegisterCommand(name = "blocktuner", permission = "blocktuner.command.base")
public class BlockTunerCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player) || !player.hasPermission("blocktuner.command.base")) {
            sender.sendMessage(Message.translatable("error.no-perm"));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(Message.translatable("error.invalid-command"));
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!player.hasPermission("blocktuner.admin")) {
                    sender.sendMessage(Message.translatable("error.no-perm"));
                    return true;
                }
                plugin.callReload();
                player.sendMessage(Message.translatable("notice.plugin-reloaded"));
            }
            if (args[0].equalsIgnoreCase("settings")) {
                if (!player.hasPermission("blocktuner.command.settings")) {
                    sender.sendMessage(Message.translatable("error.no-perm"));
                    return true;
                }
                PlayerData.of(player).toggleSetting();
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            List<String> toReturn = new ArrayList<>();
            if (sender.hasPermission("blocktuner.admin")) {
                toReturn.add("reload");
            }
            if (sender.hasPermission("blocktuner.command.settings")) {
                toReturn.add("settings");
            }
            return toReturn;
        }
        return Collections.emptyList();
    }


}
