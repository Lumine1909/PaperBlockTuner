package io.github.lumine1909.blocktuner.command;

import io.github.lumine1909.blocktuner.command.core.RegisterCommand;
import io.github.lumine1909.blocktuner.util.Message;
import io.github.lumine1909.blocktuner.util.TuneStickUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@RegisterCommand(name = "tunestick", permission = "blocktuner.command.tunestick")
public class TuneStickCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player) || !player.hasPermission("blocktuner.command.tunestick")) {
            sender.sendMessage(Message.translatable("error.no-permission"));
            return true;
        }
        if (args.length != 0) {
            sender.sendMessage(Message.translatable("error.invalid-command"));
            return true;
        }
        player.getInventory().addItem(TuneStickUtil.createTuneStick());
        player.sendMessage(Message.translatable("notice.get-tunestick"));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return Collections.emptyList();
    }
}
