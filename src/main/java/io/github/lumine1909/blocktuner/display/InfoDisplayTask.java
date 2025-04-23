package io.github.lumine1909.blocktuner.display;

import io.github.lumine1909.blocktuner.data.NoteBlockData;
import io.github.lumine1909.blocktuner.util.Message;
import io.github.lumine1909.blocktuner.util.TuneStickUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class InfoDisplayTask extends BukkitRunnable {

    public static final Map<UUID, Cache> cache = new HashMap<>();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            int i = getUpdateStatus(player);
            if (i == 0) {
                update(player, false);
            } else if (i == 2) {
                update(player, true);
            } else if (i == 1) {
                if (cache.containsKey(player.getUniqueId())) {
                    delete(player);
                }
            }
        }
    }

    private int getUpdateStatus(Player player) {
        Block target = player.getTargetBlock(null, 5);
        if (!(target.getBlockData() instanceof NoteBlock noteBlock)) {
            return 1;
        }
        ItemStack main = player.getInventory().getItemInMainHand();
        ItemStack off = player.getInventory().getItemInOffHand();
        if (!(main.getType() == Material.NOTE_BLOCK || off.getType() == Material.NOTE_BLOCK || TuneStickUtil.isTuneStick(main) || TuneStickUtil.isTuneStick(off))) {
            return 1;
        }
        if (!cache.containsKey(player.getUniqueId())) {
            return 0;
        }
        Cache cache1 = cache.get(player.getUniqueId());
        return (!cache1.noteBlock.equals(noteBlock) || !cache1.block.equals(target)) ? 0 : 2;
    }

    private void update(Player player, boolean update) {
        Block target = player.getTargetBlock(null, 5);
        if (!(target.getBlockData() instanceof NoteBlock noteBlock)) {
            return;
        }
        NoteBlockData data = new NoteBlockData(noteBlock);
        if (cache.containsKey(player.getUniqueId()) && update) {
            FakeArmorStandDisplay.updateArmor(player, Message.translatable("display.info-pattern", data.getInstrument(), data.getNote()));
        } else {
            FakeArmorStandDisplay.addArmor(player, target.getLocation().add(0.5, 1, 0.5), Message.translatable("display.info-pattern", data.getInstrument(), data.getNote()));
        }
        cache.put(player.getUniqueId(), new Cache(noteBlock, target));
    }

    private void delete(Player player) {
        FakeArmorStandDisplay.deleteArmor(player);
        cache.remove(player.getUniqueId());
    }

    public static class Cache {

        public NoteBlock noteBlock;
        public Block block;

        public Cache(NoteBlock noteBlock, Block block) {
            this.noteBlock = noteBlock;
            this.block = block;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cache cache = (Cache) o;
            if (!Objects.equals(noteBlock, cache.noteBlock)) return false;
            return Objects.equals(block, cache.block);
        }

        @Override
        public int hashCode() {
            int result = noteBlock != null ? noteBlock.hashCode() : 0;
            result = 31 * result + (block != null ? block.hashCode() : 0);
            return result;
        }
    }
}