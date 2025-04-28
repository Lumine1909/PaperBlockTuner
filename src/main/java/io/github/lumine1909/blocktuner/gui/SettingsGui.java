package io.github.lumine1909.blocktuner.gui;

import io.github.lumine1909.blocktuner.data.PlayerData;
import io.github.lumine1909.blocktuner.util.ItemBuilder;
import io.github.lumine1909.blocktuner.util.Message;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class SettingsGui implements EditingGui {

    private static final Pair<Function<PlayerData, ItemStack>, Consumer<PlayerData>> NONE = Pair.of(
        data -> new ItemStack(Material.AIR),
        data -> {
        }
    );

    private static List<Pair<Function<PlayerData, ItemStack>, Consumer<PlayerData>>> settings;
    private static ItemStack disabledIcon;

    private final Inventory inventory;

    public SettingsGui(PlayerData data) {
        this.inventory = Bukkit.createInventory(this, 9, Message.translatable("gui-name.settings"));
        for (int i = 0; i < settings.size(); i++) {
            inventory.setItem(i, settings.get(i).first().apply(data));
        }
    }

    public static void init() {
        disabledIcon = ItemBuilder.of(Material.BARRIER)
            .name(Message.translatable("settings.no-perm-name"))
            .build();

        settings = List.of(
            NONE, NONE,
            createToggleSetting(
                "blocktuner.settings.note-tuning",
                Material.STICK,
                "settings.note",
                data -> data.enableStickNoteTuning,
                (data, value) -> data.enableStickNoteTuning = value
            ),
            createToggleSetting(
                "blocktuner.settings.instrument-tuning",
                Material.GOLD_BLOCK,
                "settings.instrument",
                data -> data.enableStickInstrumentTuning,
                (data, value) -> data.enableStickInstrumentTuning = value
            ),
            createToggleSetting(
                "blocktuner.settings.sync-instrument",
                Material.PACKED_ICE,
                "settings.sync-instrument",
                data -> data.syncBlockInstrument,
                (data, value) -> data.syncBlockInstrument = value
            ),
            createToggleSetting(
                "blocktuner.settings.scroll-item",
                Material.COMPASS,
                "settings.scroll-item",
                data -> data.enableItemScrollTuning,
                (data, value) -> data.enableItemScrollTuning = value
            ),
            createToggleSetting(
                "blocktuner.settings.scroll-block",
                Material.NOTE_BLOCK,
                "settings.scroll-block",
                data -> data.enableBlockScrollTuning,
                (data, value) -> data.enableBlockScrollTuning = value
            ),
            NONE, NONE
        );
    }

    private static Pair<Function<PlayerData, ItemStack>, Consumer<PlayerData>> createToggleSetting(
        String permission,
        Material icon,
        String nameKey,
        Function<PlayerData, Boolean> getter,
        BiConsumer<PlayerData, Boolean> setter
    ) {
        return Pair.of(
            data -> {
                if (!data.player.hasPermission(permission)) return disabledIcon;
                boolean enabled = getter.apply(data);
                return ItemBuilder.of(icon)
                    .name(Message.translatable(nameKey))
                    .fakeEnch(enabled)
                    .lore(List.of(enabled ? Message.translatable("settings.enabled") : Message.translatable("settings.disabled")))
                    .build();
            },
            data -> {
                if (data.player.hasPermission(permission)) {
                    setter.accept(data, !getter.apply(data));
                }
            }
        );
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        int slot = event.getRawSlot();
        if (slot < 0 || slot >= inventory.getSize()) {
            return;
        }
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        PlayerData data = PlayerData.of(player);
        if (slot < settings.size()) {
            Pair<Function<PlayerData, ItemStack>, Consumer<PlayerData>> pair = settings.get(slot);
            pair.second().accept(data);
            inventory.setItem(slot, pair.first().apply(data));
        }
    }
}