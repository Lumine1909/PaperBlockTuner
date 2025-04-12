package io.github.lumine1909.blocktuner.listener;

import io.github.lumine1909.blocktuner.util.TuneUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import static io.github.lumine1909.blocktuner.BlockTunerPlugin.plugin;

public class NotePlayListener implements Listener {

    public NotePlayListener() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("blocktuner.playnote") ||
            event.getClickedBlock() == null ||
            !(event.getClickedBlock().getBlockData() instanceof NoteBlock) ||
            !event.getAction().isRightClick()) {
            return;
        }
        if (player.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD) {
            TuneUtil.play(player, player.getWorld(), event.getClickedBlock().getLocation());
            event.setCancelled(true);
        }
    }
}
