package io.github.lumine1909.blocktuner.display;

import io.github.lumine1909.blocktuner.data.NoteBlockData;
import io.github.lumine1909.blocktuner.util.Message;
import io.github.lumine1909.blocktuner.util.TuneStickUtil;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class InfoDisplayTask extends BukkitRunnable {

    public static final Map<UUID, Cache> cacheTrackMap = new HashMap<>();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            getUpdateStatus(player).runTask(player);
        }
    }

    private static UpdateStatus getUpdateStatus(Player player) {
        Block target = player.getTargetBlockExact(5);
        if (target == null || !(target.getBlockData() instanceof NoteBlock noteBlock)) {
            return UpdateStatus.DELETE;
        }
        ItemStack main = player.getInventory().getItemInMainHand();
        ItemStack off = player.getInventory().getItemInOffHand();
        if (!(main.getType() == Material.NOTE_BLOCK || off.getType() == Material.NOTE_BLOCK || TuneStickUtil.isTuneStick(main) || TuneStickUtil.isTuneStick(off))) {
            return UpdateStatus.DELETE;
        }
        if (!cacheTrackMap.containsKey(player.getUniqueId())) {
            return UpdateStatus.CREATE;
        }
        Cache cache = cacheTrackMap.get(player.getUniqueId());
        return cache.getStatus(noteBlock, target);
    }

    private static void updateInfoDisplay(Player player, boolean update) {
        Block target = player.getTargetBlock(null, 5);
        if (!(target.getBlockData() instanceof NoteBlock noteBlock)) {
            return;
        }
        NoteBlockData data = new NoteBlockData(noteBlock);
        if (cacheTrackMap.containsKey(player.getUniqueId()) && update) {
            FakeArmorStandDisplay.updateArmorDisplay(player, Message.translatable("display.info-pattern", data.getInstrument(), data.getNote()));
        } else {
            FakeArmorStandDisplay.addArmorDisplay(player, target.getLocation().add(0.5, 1, 0.5), Message.translatable("display.info-pattern", data.getInstrument(), data.getNote()));
        }
        cacheTrackMap.put(player.getUniqueId(), new Cache(target, noteBlock.getInstrument(), noteBlock.getNote().getId()));
    }

    private static void delete(Player player) {
        FakeArmorStandDisplay.deleteArmorDisplay(player);
        cacheTrackMap.remove(player.getUniqueId());
    }

    record Cache(Block block, Instrument instrument, byte note) {

        public UpdateStatus getStatus(NoteBlock noteBlock, Block block) {
            if (!this.block.equals(block)) {
                return UpdateStatus.CREATE;
            }
            if (this.instrument.equals(noteBlock.getInstrument()) && this.note == noteBlock.getNote().getId()) {
                return UpdateStatus.SKIP;
            }
            return UpdateStatus.UPDATE;
        }
    }

    enum UpdateStatus {
        CREATE(player -> updateInfoDisplay(player, false)),
        DELETE(player -> {
            if (cacheTrackMap.containsKey(player.getUniqueId())) {
                delete(player);
            }
        }),
        UPDATE(player -> updateInfoDisplay(player, true)),
        SKIP(player -> {});

        private final Consumer<Player> task;

        UpdateStatus(Consumer<Player> task) {
            this.task = task;
        }

        public void runTask(Player player) {
            task.accept(player);
        }
    }
}