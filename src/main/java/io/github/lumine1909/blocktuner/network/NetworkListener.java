package io.github.lumine1909.blocktuner.network;

import io.github.lumine1909.blocktuner.network.misc.ChannelHandler;
import io.netty.channel.Channel;
import io.papermc.paper.network.ChannelInitializeListenerHolder;
import net.kyori.adventure.key.Key;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NetworkListener {

    private static final Key INITLISTENER_KEY = Key.key("paperblocktuer:register");

    public static void registerListener() {
        ChannelInitializeListenerHolder.addListener(
            INITLISTENER_KEY,
            channel -> channel.pipeline().addBefore("packet_handler", "blocktuner_handler", new ChannelHandler(channel))
        );
    }

    public static void unregisterListener() {
        ChannelInitializeListenerHolder.removeListener(INITLISTENER_KEY);
    }

    public static void registerChannel(Player player) {
        ServerPlayer sp = ((CraftPlayer) player).getHandle();
        if (sp.connection.connection.channel.pipeline().get("blocktuner_handler") == null) {
            sp.connection.connection.channel.pipeline().addBefore("packet_handler", "blocktuner_handler", new ChannelHandler(sp));
        }
    }

    public static void unregisterChannel(Player player) {
        ServerPlayer sp = ((CraftPlayer) player).getHandle();
        if (sp.connection.connection.channel.pipeline().get("blocktuner_handler") != null) {
            sp.connection.connection.channel.pipeline().remove("blocktuner_handler");
        }
    }
}