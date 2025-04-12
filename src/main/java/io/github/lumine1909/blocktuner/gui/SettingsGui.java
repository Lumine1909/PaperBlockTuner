package io.github.lumine1909.blocktuner.gui;

import io.github.lumine1909.blocktuner.data.PlayerData;
import io.github.lumine1909.blocktuner.util.ItemBuilder;
import io.github.lumine1909.blocktuner.util.Message;
import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class SettingsGui implements EditingGui {

    private static final Pair<Function<PlayerData, ItemStack>, Consumer<PlayerData>> NONE = Pair.of(
        data -> new ItemStack(Material.AIR),
        data -> {
        }
    );
    private static ItemStack DISABLED;
    private static List<Pair<Function<PlayerData, ItemStack>, Consumer<PlayerData>>> SETTINGS_HELPER;
    public final Inventory INVENTORY;

    public SettingsGui(PlayerData data) {
        INVENTORY = Bukkit.createInventory(this, 9, Message.translatable("gui-name.settings"));
        int i = 0;
        for (Pair<Function<PlayerData, ItemStack>, Consumer<PlayerData>> pair : SETTINGS_HELPER) {
            INVENTORY.setItem(i, pair.first().get(data));
            i++;
        }
    }

    public static void init() {
        DISABLED = ItemBuilder.of(Material.BARRIER)
            .name(Message.translatable("settings.no-perm-name"))
            .build();
        SETTINGS_HELPER = List.of(
            NONE,
            NONE,
            Pair.of(
                data -> {
                    PlayerData playerData = (PlayerData) data;
                    if (!playerData.player.hasPermission("blocktuner.settings.note-tuning")) {
                        return DISABLED;
                    }
                    return ItemBuilder.of(Material.STICK)
                        .name(Message.translatable("settings.note"))
                        .fakeEnch(playerData.enableStickNoteTuning)
                        .lore(List.of(playerData.enableStickNoteTuning ? Message.translatable("settings.enabled") : Message.translatable("settings.disabled")))
                        .build();
                },
                data -> {
                    if (data.player.hasPermission("blocktuner.settings.note-tuning")) {
                        data.enableStickNoteTuning = !data.enableStickNoteTuning;
                    }
                }
            ),
            Pair.of(
                data -> {
                    PlayerData playerData = (PlayerData) data;
                    if (!playerData.player.hasPermission("blocktuner.settings.instrument-tuning")) {
                        return DISABLED;
                    }
                    return ItemBuilder.of(Material.GOLD_BLOCK)
                        .name(Message.translatable("settings.instrument"))
                        .fakeEnch(playerData.enableStickInstrumentTuning)
                        .lore(List.of(playerData.enableStickInstrumentTuning ? Message.translatable("settings.enabled") : Message.translatable("settings.disabled")))
                        .build();
                },
                data -> {
                    if (data.player.hasPermission("blocktuner.settings.instrument-tuning")) {
                        data.enableStickInstrumentTuning = !data.enableStickInstrumentTuning;
                    }
                }
            ),
            Pair.of(
                data -> {
                    PlayerData playerData = (PlayerData) data;
                    if (!playerData.player.hasPermission("blocktuner.settings.sync-instrument")) {
                        return DISABLED;
                    }
                    return ItemBuilder.of(Material.PACKED_ICE)
                        .name(Message.translatable("settings.sync-instrument"))
                        .fakeEnch(playerData.syncBlockInstrument)
                        .lore(List.of(playerData.syncBlockInstrument ? Message.translatable("settings.enabled") : Message.translatable("settings.disabled")))
                        .build();
                },
                data -> {
                    if (data.player.hasPermission("blocktuner.settings.sync-instrument")) {
                        data.syncBlockInstrument = !data.syncBlockInstrument;
                    }
                }
            ),

            Pair.of(
                data -> {
                    PlayerData playerData = (PlayerData) data;
                    if (!playerData.player.hasPermission("blocktuner.settings.scroll-item")) {
                        return DISABLED;
                    }
                    return ItemBuilder.of(Material.COMPASS)
                        .name(Message.translatable("settings.scroll-item"))
                        .fakeEnch(playerData.enableItemScrollTuning)
                        .lore(List.of(playerData.enableItemScrollTuning ? Message.translatable("settings.enabled") : Message.translatable("settings.disabled")))
                        .build();
                },
                data -> {
                    if (data.player.hasPermission("blocktuner.settings.scroll-item")) {
                        data.enableItemScrollTuning = !data.enableItemScrollTuning;
                    }
                }
            ),
            Pair.of(
                data -> {
                    PlayerData playerData = (PlayerData) data;
                    if (!playerData.player.hasPermission("blocktuner.settings.scroll-block")) {
                        return DISABLED;
                    }
                    return ItemBuilder.of(Material.NOTE_BLOCK)
                        .name(Message.translatable("settings.scroll-block"))
                        .fakeEnch(playerData.enableBlockScrollTuning)
                        .lore(List.of(playerData.enableBlockScrollTuning ? Message.translatable("settings.enabled") : Message.translatable("settings.disabled")))
                        .build();
                },
                data -> {
                    if (data.player.hasPermission("blocktuner.settings.scroll-block")) {
                        data.enableBlockScrollTuning = !data.enableBlockScrollTuning;
                    }
                }
            ),
            NONE,
            NONE
        );
    }

    @Override
    public @NotNull Inventory getInventory() {
        return INVENTORY;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        int slot = event.getRawSlot();
        if (slot < 0 || slot >= INVENTORY.getSize()) {
            return;
        }
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        PlayerData data = PlayerData.of(player);
        SETTINGS_HELPER.get(slot).second().accept(data);
        INVENTORY.setItem(slot, SETTINGS_HELPER.get(slot).first().get(data));
    }
}