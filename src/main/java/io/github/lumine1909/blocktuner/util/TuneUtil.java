package io.github.lumine1909.blocktuner.util;

import io.github.lumine1909.blocktuner.data.PlayerData;
import io.github.lumine1909.blocktuner.object.Instrument;
import io.github.lumine1909.blocktuner.object.Note;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.gameevent.GameEvent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TuneUtil {

    public static void tune(Player player, Block block, Note note, Instrument instrument) {
        tune(
            ((CraftPlayer) player).getHandle(),
            ((CraftWorld) block.getWorld()).getHandle(),
            new BlockPos(block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()),
            note,
            instrument
        );
    }

    public static void tune(ServerPlayer player, ServerLevel world, BlockPos pos, Note note, Instrument instrument) {
        BlockState state = world.getBlockState(pos);
        if (note != Note.EMPTY) {
            state = state.setValue(NoteBlock.NOTE, note.getNote());
        }
        if (instrument == Instrument.DEFAULT) {
            state = state.setValue(NoteBlock.INSTRUMENT, getInstrumentFor(world, pos));
        } else if (instrument != Instrument.EMPTY) {
            state = state.setValue(NoteBlock.INSTRUMENT, NoteBlockInstrument.valueOf(instrument.name().toUpperCase()));
            if (PlayerData.of(player.getBukkitEntity()).syncBlockInstrument) {
                world.getWorld().getBlockAt(new Location(world.getWorld(), pos.getX(), pos.getY() - 1, pos.getZ()))
                    .setType(instrument.getMaterial(), false);
            }
        }
        world.setBlock(pos, state, 2 | 16 | 1024);
        play(player, world, pos);
    }

    public static void play(Player player, World world, Location location) {
        play(
            ((CraftPlayer) player).getHandle(),
            ((CraftWorld) world).getHandle(),
            new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ())
        );
    }

    public static void play(ServerPlayer player, ServerLevel world, BlockPos pos) {
        if (world.getBlockState(pos.above()).isAir() || (world.getBlockState(pos).getValue(NoteBlock.INSTRUMENT)).worksAboveNoteBlock()) {
            NoteBlock block = (NoteBlock) world.getBlockState(pos).getBlock();
            world.blockEvent(pos, block, 0, 0);
            world.gameEvent(player, GameEvent.NOTE_BLOCK_PLAY, pos);
        }
        player.swing(InteractionHand.MAIN_HAND);
    }

    private static NoteBlockInstrument getInstrumentFor(LevelReader level, BlockPos pos) {
        NoteBlockInstrument noteBlockInstrument = level.getBlockState(pos.above()).instrument();
        if (noteBlockInstrument.worksAboveNoteBlock()) {
            return noteBlockInstrument;
        } else {
            NoteBlockInstrument noteBlockInstrument1 = level.getBlockState(pos.below()).instrument();
            return noteBlockInstrument1.worksAboveNoteBlock() ? NoteBlockInstrument.HARP : noteBlockInstrument1;
        }
    }
}