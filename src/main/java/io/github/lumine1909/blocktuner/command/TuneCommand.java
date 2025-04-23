package io.github.lumine1909.blocktuner.command;

import io.github.lumine1909.blocktuner.command.core.RegisterCommand;
import io.github.lumine1909.blocktuner.data.NoteBlockData;
import io.github.lumine1909.blocktuner.util.CommandUtil;
import io.github.lumine1909.blocktuner.util.Message;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@RegisterCommand(name = "tune", permission = "blocktuner.command.tune")
public class TuneCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player) || !player.hasPermission("blocktuner.command.tune")) {
            sender.sendMessage(Message.translatable("error.no-permission"));
            return true;
        }
        if (args.length == 0 || args.length > 2) {
            sender.sendMessage(Message.translatable("error.invalid-command"));
            return true;
        }
        Block target;
        if ((target = player.getTargetBlock(null, 5)).isEmpty()) {
            sender.sendMessage(Message.translatable("error.not-facing-noteblock"));
            return true;
        }
        if (!(target.getBlockData() instanceof NoteBlock noteBlock)) {
            sender.sendMessage(Message.translatable("error.not-facing-noteblock"));
            return true;
        }
        NoteBlockData.parseData(noteBlock, sender, args).apply(player, target);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            if (args[0].startsWith("note=")) {
                return CommandUtil.getMatched(Message.SET_NOTE_SUGGESTIONS, args[0]);
            }
            if (args[0].startsWith("instrument=")) {
                return CommandUtil.getMatched(Message.SET_INSTRUMENT_SUGGESTIONS, args[0]);
            }
            return List.of("note=", "instrument=");
        }
        if (args.length == 2) {
            if (args[0].startsWith("note=")) {
                if (args[1].startsWith("instrument=")) {
                    return CommandUtil.getMatched(Message.SET_INSTRUMENT_SUGGESTIONS, args[1]);
                }
                return Collections.singletonList("instrument=");
            }
            if (args[0].startsWith("instrument=")) {
                if (args[1].startsWith("note=")) {
                    return CommandUtil.getMatched(Message.SET_NOTE_SUGGESTIONS, args[1]);
                }
                return Collections.singletonList("note=");
            }
        }
        return Collections.emptyList();
    }
}