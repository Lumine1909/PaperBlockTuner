package io.github.lumine1909.blocktuner.data;

import io.github.lumine1909.blocktuner.object.Instrument;
import io.github.lumine1909.blocktuner.object.Note;
import io.github.lumine1909.blocktuner.util.InstrumentUtil;
import io.github.lumine1909.blocktuner.util.Message;
import io.github.lumine1909.blocktuner.util.NoteUtil;
import io.github.lumine1909.blocktuner.util.TuneUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;

public class NoteBlockData {

    private static final NoteBlockData EMPTY = new NoteBlockData(Note.EMPTY, Instrument.EMPTY);

    private Note note;
    private Instrument instrument;

    public NoteBlockData(int note, Instrument instrument) {
        this(NoteUtil.byNote(note), instrument);
    }

    public NoteBlockData(Note note, Instrument instrument) {
        this.note = note;
        this.instrument = instrument;
    }

    public NoteBlockData(NoteBlock noteBlock) {
        this(noteBlock.getNote().getId(), InstrumentUtil.byBkInstrument(noteBlock.getInstrument()));
    }

    public NoteBlockData(int note) {
        this(note, null);
    }

    public static NoteBlockData parseData(NoteBlock origin, CommandSender sender, String[] args) {
        NoteBlockData data = new NoteBlockData(origin);
        for (String arg : args) {
            if (arg.startsWith("note=")) {
                String realArg = arg.substring("note=".length());
                Note note = NoteUtil.byName(realArg) == null ? NoteUtil.byId(realArg) : NoteUtil.byName(realArg);
                if (note == null) {
                    sender.sendMessage(Message.translatable("error.invalid-note"));
                    return EMPTY;
                }
                data.setNote(note);
            }
            if (arg.startsWith("instrument=")) {
                String realArg = arg.substring("instrument=".length());
                Instrument instrument = InstrumentUtil.byMcName(realArg.toLowerCase());
                if (instrument == null) {
                    sender.sendMessage(Message.translatable("error.invalid-instrument"));
                    return EMPTY;
                }
                data.setInstrument(instrument);
            }
        }
        return data;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public void apply(Player player, Block block) {
        if (note == Note.EMPTY && instrument == Instrument.EMPTY) {
            return;
        }
        TuneUtil.tune(player, block, note, instrument);
    }

    public ItemStack apply(ItemStack itemStack) {
        itemStack.editMeta(itemMeta -> {
            if (note == Note.EMPTY && instrument == Instrument.EMPTY) {
                return;
            }
            if (!(itemMeta instanceof BlockDataMeta blockDataMeta)) {
                return;
            }

            NoteBlock noteBlock = (NoteBlock) blockDataMeta.getBlockData(Material.NOTE_BLOCK);
            NoteBlockData origin = new NoteBlockData(noteBlock);
            if (note != Note.EMPTY) {
                origin.setNote(note);
            }
            if (instrument != Instrument.EMPTY) {
                origin.setInstrument(instrument);
            }
            apply(noteBlock);
            itemMeta.displayName(Message.translatable("item.noteblock-name", instrument, origin.getNote()));
            itemMeta.addEnchant(Enchantment.UNBREAKING, 0, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            blockDataMeta.setBlockData(noteBlock);
        });
        if (instrument == Instrument.DEFAULT) {
            itemStack = InstrumentUtil.stripInstrument(itemStack);
        }
        return itemStack;
    }

    public void apply(NoteBlock noteBlock) {
        if (note != Note.EMPTY) {
            noteBlock.setNote(new org.bukkit.Note(note.getNote()));
        }
        if (instrument != Instrument.EMPTY) {
            noteBlock.setInstrument(instrument.getBukkitInstrument());
        }
    }
}