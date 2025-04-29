package io.github.lumine1909.blocktuner.network.misc;

import io.github.lumine1909.blocktuner.network.BlockTunerProtocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ServerboundPickItemFromBlockPacket;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class ChannelHandler extends ChannelDuplexHandler {

    private final PacketContext context;

    public ChannelHandler(ServerPlayer sp) {
        this.context = new PacketContext(sp);
    }

    public ChannelHandler(Channel channel) {
        this.context = new PacketContext(channel);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ServerboundHelloPacket(String name, UUID uuid)) {
            this.context.setName(name);
        }
        if (msg instanceof ServerboundCustomPayloadPacket(CustomPacketPayload payload) &&
            BlockTunerProtocol.handleModPacket(payload, context)) {
            return;
        }
        if (msg instanceof ServerboundPickItemFromBlockPacket packet &&
            BlockTunerProtocol.handlePickItemPacket(packet, context)) {
            return;
        }
        super.channelRead(ctx, msg);
    }
}