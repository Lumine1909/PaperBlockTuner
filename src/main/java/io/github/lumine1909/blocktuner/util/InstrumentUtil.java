package io.github.lumine1909.blocktuner.util;

import io.github.lumine1909.blocktuner.object.Instrument;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.NoteBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class InstrumentUtil {

    public static Instrument byMcName(String name) {
        return Instrument.NAME_MAP.get(name);
    }

    public static Instrument byBkInstrument(org.bukkit.Instrument instrument) {
        return Instrument.INSTRUMENT_MAP.get(instrument);
    }

    public static String getName(Instrument instrument) {
        return Message.translate("instrument." + instrument.getName());
    }

    public static ItemStack stripInstrument(ItemStack stack) {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(stack);
        BlockItemStateProperties properties = nmsItemStack.get(DataComponents.BLOCK_STATE);
        if (properties == null) {
            return stack;
        }
        Integer note;
        if ((note = properties.get(NoteBlock.NOTE)) != null) {
            nmsItemStack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(NoteBlock.NOTE, note));
        }
        return CraftItemStack.asBukkitCopy(nmsItemStack);
    }
}
