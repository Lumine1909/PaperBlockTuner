package io.github.lumine1909.blocktuner.command;

import io.github.lumine1909.blocktuner.command.core.RegisterCommand;
import io.github.lumine1909.blocktuner.data.NoteBlockData;
import io.github.lumine1909.blocktuner.util.Message;
import org.bukkit.Material;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@RegisterCommand(name = "tunehand", permission = "blocktuner.command.tunehand")
public class TuneHandCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player) || !player.hasPermission("blocktuner.command.tunehand")) {
            sender.sendMessage(Message.translatable("error.no-perm"));
            return true;
        }
        if (args.length == 0 || args.length > 2) {
            sender.sendMessage(Message.translatable("error.invalid-command"));
            return true;
        }
        ItemStack itemStack;
        if ((itemStack = player.getInventory().getItemInMainHand()).isEmpty()) {
            sender.sendMessage(Message.translatable("error.not-handing-noteblock"));
            return true;
        }
        if (itemStack.getType() != Material.NOTE_BLOCK) {
            sender.sendMessage(Message.translatable("error.not-handing-noteblock"));
            return true;
        }
        NoteBlock noteBlock = (NoteBlock) ((BlockDataMeta) itemStack.getItemMeta()).getBlockData(Material.NOTE_BLOCK);
        itemStack = NoteBlockData.parseData(noteBlock, sender, args).apply(itemStack);
        player.getInventory().setItemInMainHand(itemStack);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            if (args[0].startsWith("note=")) {
                return Message.SET_NOTE_SUGGESTIONS;
            }
            if (args[0].startsWith("instrument=")) {
                return Message.SET_INSTRUMENT_SUGGESTIONS;
            }
            return List.of("note=", "instrument=");
        }
        if (args.length == 2) {
            if (args[0].startsWith("note=")) {
                if (args[1].startsWith("instrument=")) {
                    return Message.SET_INSTRUMENT_SUGGESTIONS;
                }
                return Collections.singletonList("instrument=");
            }
            if (args[0].startsWith("instrument=")) {
                if (args[1].startsWith("note=")) {
                    return Message.SET_NOTE_SUGGESTIONS;
                }
                return Collections.singletonList("note=");
            }
        }
        return Collections.emptyList();
    }
}