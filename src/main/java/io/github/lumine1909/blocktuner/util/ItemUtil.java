package io.github.lumine1909.blocktuner.util;

import io.github.lumine1909.blocktuner.data.NoteBlockData;
import io.github.lumine1909.blocktuner.object.Instrument;
import io.github.lumine1909.blocktuner.object.Note;
import org.bukkit.Material;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;

public class ItemUtil {

    public static Note getNote(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() != Material.NOTE_BLOCK) {
            return Note.EMPTY;
        }
        return NoteUtil.byNote(((NoteBlock) ((BlockDataMeta) itemStack.getItemMeta()).getBlockData(Material.NOTE_BLOCK)).getNote().getId());
    }

    public static ItemStack setNote(ItemStack itemStack, Note note) {
        if (itemStack == null || itemStack.getType() != Material.NOTE_BLOCK) {
            return itemStack;
        }
        return new NoteBlockData(note, Instrument.EMPTY).apply(itemStack);
    }

    public static Instrument getInstrument(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() != Material.NOTE_BLOCK) {
            return Instrument.EMPTY;
        }
        return InstrumentUtil.byBkInstrument(((NoteBlock) ((BlockDataMeta) itemStack.getItemMeta()).getBlockData(Material.NOTE_BLOCK)).getInstrument());
    }

    public static ItemStack setInstrument(ItemStack itemStack, Instrument instrument) {
        if (itemStack == null || itemStack.getType() != Material.NOTE_BLOCK) {
            return itemStack;
        }
        return new NoteBlockData(Note.EMPTY, instrument).apply(itemStack);
    }
}
