package io.github.lumine1909.blocktuner.listener;

import io.github.lumine1909.blocktuner.network.BlockTunerProtocol;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ServerboundPickItemFromBlockPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static io.github.lumine1909.blocktuner.BlockTunerPlugin.plugin;

public class NetworkListener implements Listener {

    public NetworkListener() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static void registerChannel(Player player) {
        ServerPlayer sp = ((CraftPlayer) player).getHandle();
        sp.connection.connection.channel.pipeline().addBefore("packet_handler", "blocktuner_handler", new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if (msg instanceof ServerboundCustomPayloadPacket(
                    CustomPacketPayload payload
                ) && BlockTunerProtocol.handleModPacket(payload, sp)) {
                    return;
                }
                if (msg instanceof ServerboundPickItemFromBlockPacket packet && BlockTunerProtocol.handlePickItemPacket(packet, sp)) {
                    return;
                }
                super.channelRead(ctx, msg);
            }

            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                super.write(ctx, msg, promise);
            }
        });
    }

    public static void unregisterChannel(Player player) {
        ServerPlayer sp = ((CraftPlayer) player).getHandle();
        sp.connection.connection.channel.pipeline().remove("blocktuner_handler");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        registerChannel(event.getPlayer());
    }
}
