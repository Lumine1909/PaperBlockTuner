package io.github.lumine1909.blocktuner.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    private ItemBuilder(Material material, int count) {
        itemStack = new ItemStack(material, Math.max(count, 1));
        itemMeta = itemStack.getItemMeta();
    }

    public static ItemBuilder of(Material material) {
        return of(material, 1);
    }

    public static ItemBuilder of(Material material, int count) {
        return new ItemBuilder(material, count);
    }

    public ItemBuilder name(String str) {
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', str));
        return this;
    }

    public ItemBuilder name(Component component) {
        itemMeta.displayName(component.decoration(TextDecoration.ITALIC, false));
        return this;
    }

    public ItemBuilder lore(String... lore) {
        itemMeta.setLore(Arrays.stream(lore).map(l -> ChatColor.translateAlternateColorCodes('&', l)).collect(Collectors.toList()));
        return this;
    }

    public ItemBuilder lore(List<Component> lore) {
        itemMeta.lore(lore);
        return this;
    }

    public ItemBuilder fakeEnch(boolean flag) {
        itemMeta.setEnchantmentGlintOverride(flag);
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
