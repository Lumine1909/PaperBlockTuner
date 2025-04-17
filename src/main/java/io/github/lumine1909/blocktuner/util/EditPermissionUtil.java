package io.github.lumine1909.blocktuner.util;

import com.plotsquared.core.plot.Plot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class EditPermissionUtil {

    public static boolean canEdit(Player player, Location location) {
        if (player.hasPermission("blocktuner.admin")) {
            return true;
        }
        if (hasPlotSquared()) {
            Plot plot = Plot.getPlot(
                com.plotsquared.core.location.Location.at(
                    location.getWorld().getName(),
                    location.getBlockX(), location.getBlockY(), location.getBlockZ()
                ));
            if (plot != null) {
                return plot.getMembers().contains(player.getUniqueId()) ||
                    plot.getOwners().contains(player.getUniqueId()) ||
                    plot.getTrusted().contains(player.getUniqueId());
            }
        }
        return player.hasPermission("blocktuner.edit");
    }

    private static boolean hasPlotSquared() {
        Plugin plotSquared = Bukkit.getPluginManager().getPlugin("PlotSquared");
        return plotSquared != null && plotSquared.isEnabled();
    }
}