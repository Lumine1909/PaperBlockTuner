package io.github.lumine1909.blocktuner.listener;

import io.github.lumine1909.blocktuner.data.PlayerData;
import io.github.lumine1909.blocktuner.util.EditPermissionUtil;
import io.github.lumine1909.blocktuner.util.TuneStickUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import static io.github.lumine1909.blocktuner.BlockTunerPlugin.plugin;

public class StickInteractListener implements Listener {

    public StickInteractListener() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!TuneStickUtil.isTuneStick(player.getInventory().getItemInMainHand()) && !TuneStickUtil.isTuneStick(player.getInventory().getItemInOffHand())) {
            return;
        }
        PlayerData data = PlayerData.of(player);
        Block target;
        if ((target = player.getTargetBlock(null, 5)).getBlockData() instanceof NoteBlock noteBlock &&
            EditPermissionUtil.canEdit(player, target.getLocation())) {
            data.startEdit(target, noteBlock, event);
        }
    }
}
