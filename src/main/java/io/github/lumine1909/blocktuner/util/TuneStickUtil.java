package io.github.lumine1909.blocktuner.util;

import net.kyori.adventure.text.Component;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TuneStickUtil {

    public static boolean isTuneStick(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        CustomData data;
        if ((data = nmsItemStack.get(DataComponents.CUSTOM_DATA)) != null) {
            return data.contains("isTuneStick");
        }
        return false;
    }

    public static ItemStack createTuneStick() {
        ItemStack itemStack = new ItemStack(Material.STICK);
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("isTuneStick", true);
        nmsItemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        itemStack = CraftItemStack.asBukkitCopy(nmsItemStack);
        itemStack.editMeta(meta -> {
            meta.displayName(Message.translatable("item.tunestick-name"));
            List<? extends Component> lore = List.of(Component.empty(), Message.translatable("item.tunestick-lore"));
            meta.lore(lore);
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        });
        return itemStack;
    }
}
