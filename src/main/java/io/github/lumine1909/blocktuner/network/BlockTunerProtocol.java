package io.github.lumine1909.blocktuner.network;

import io.github.lumine1909.blocktuner.data.NoteBlockData;
import io.github.lumine1909.blocktuner.object.Instrument;
import io.github.lumine1909.blocktuner.util.InstrumentUtil;
import io.github.lumine1909.blocktuner.util.NoteUtil;
import io.github.lumine1909.blocktuner.util.TuneUtil;
import io.github.lumine1909.messageutil.api.MessageReceiver;
import io.github.lumine1909.messageutil.object.PacketContext;
import io.github.lumine1909.messageutil.object.PacketEvent;
import io.netty.buffer.Unpooled;
import io.papermc.paper.configuration.GlobalConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.DiscardedPayload;
import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;
import net.minecraft.network.protocol.game.ServerboundPickItemFromBlockPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static io.github.lumine1909.blocktuner.BlockTunerPlugin.plugin;

public class BlockTunerProtocol extends MessageReceiver {

    public static final String MOD_ID = "blocktuner";
    private static final int TUNING_PROTOCOL = 3;

    public static final String CLIENT_BOUND_HELLO = "blocktuner:client_bound_hello";
    public static final String SERVER_BOUND_HELLO = "blocktuner:server_bound_hello";
    public static final String SERVER_BOUND_TUNING = "blocktuner:server_bound_tuning";

    @Override
    public boolean isActive() {
        return plugin.isEnabled();
    }

    @MessageReceiver.Bytebuf(key = SERVER_BOUND_HELLO)
    public void handleHello(PacketContext context, PacketEvent event, FriendlyByteBuf buf) {
        int protocolVersion = buf.readInt();
        if (protocolVersion == TUNING_PROTOCOL) {
            event.setCancelled(true);
            context.send(CLIENT_BOUND_HELLO, buf);
        }
    }

    @MessageReceiver.Bytebuf(key = SERVER_BOUND_TUNING)
    public void handleTuning(PacketContext context, PacketEvent event, FriendlyByteBuf buf) {
        event.setCancelled(true);
        ServerPlayer player = context.player().orElseThrow();
        BlockPos pos = buf.readBlockPos();
        int note = buf.readInt();
        Level world = player.level();
        if (world.getBlockState(pos).getBlock() == Blocks.NOTE_BLOCK) {
            Bukkit.getScheduler().runTask(plugin, () -> TuneUtil.tune(player, (ServerLevel) world, pos, NoteUtil.byNote(note), Instrument.DEFAULT));
        }
    }

    @MessageReceiver.Vanilla(packetType = ServerboundPickItemFromBlockPacket.class)
    public void handlePickItem(PacketContext context, PacketEvent event, ServerboundPickItemFromBlockPacket packet) {
        if (!packet.includeData()) {
            return;
        }
        ServerPlayer player = context.player().orElseThrow();
        ServerLevel serverLevel = player.serverLevel();
        BlockPos blockPos = packet.pos();
        if (!player.canInteractWithBlock(blockPos, 1.0F) || !serverLevel.isLoaded(blockPos)) {
            return;
        }
        BlockState blockState = serverLevel.getBlockState(blockPos);
        if (!(blockState.getBlock() instanceof NoteBlock)) {
            return;
        }
        boolean flag = player.hasInfiniteMaterials() && packet.includeData();
        ItemStack cloneItemStack = blockState.getCloneItemStack(serverLevel, blockPos, flag);
        event.setCancelled(true);
        if (cloneItemStack.isEmpty()) {
            return;
        }
        if (flag && player.getBukkitEntity().hasPermission("minecraft.nbt.copy")) {
            addBlockDataToItem(blockState, serverLevel, blockPos, cloneItemStack);
        }
        final ItemStack toPick;
        if (flag) {
            org.bukkit.inventory.ItemStack bukkitItemStack = CraftItemStack.asBukkitCopy(cloneItemStack);
            NoteBlockInstrument instrument = blockState.getValue(NoteBlock.INSTRUMENT);
            Integer note = blockState.getValue(NoteBlock.NOTE);
            NoteBlockData data = new NoteBlockData(note, InstrumentUtil.byMcName(instrument.name().toLowerCase()));
            bukkitItemStack = data.apply(bukkitItemStack);
            toPick = CraftItemStack.asNMSCopy(bukkitItemStack);
        } else {
            toPick = cloneItemStack;
        }
        Bukkit.getScheduler().runTask(plugin, () -> tryPickItem(toPick, player));
    }

    private static void addBlockDataToItem(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack) {
        BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        if (blockEntity != null) {
            CompoundTag compoundTag = blockEntity.saveCustomOnly(level.registryAccess());
            blockEntity.removeComponentsFromTag(compoundTag);
            BlockItem.setBlockEntityData(stack, blockEntity.getType(), compoundTag);
            stack.applyComponents(blockEntity.collectComponents());
        }
    }

    private static void tryPickItem(ItemStack stack, ServerPlayer player) {
        if (!stack.isItemEnabled(player.level().enabledFeatures())) {
            return;
        }
        Inventory inventory = player.getInventory();
        int sourceSlot = inventory.findSlotMatchingItem(stack);
        int targetSlot = Inventory.isHotbarSlot(sourceSlot) ? sourceSlot : inventory.getSuitableHotbarSlot();
        if (sourceSlot != -1) {
            if (Inventory.isHotbarSlot(sourceSlot) && Inventory.isHotbarSlot(targetSlot)) {
                inventory.selected = targetSlot;
            } else {
                inventory.pickSlot(sourceSlot, targetSlot);
            }
        } else if (player.hasInfiniteMaterials()) {
            inventory.addAndPickItem(stack, targetSlot);
        }
        player.connection.send(new ClientboundSetHeldSlotPacket(inventory.selected));
        player.inventoryMenu.broadcastChanges();
        if (GlobalConfiguration.get().unsupportedSettings.updateEquipmentOnPlayerActions) {
            player.detectEquipmentUpdatesPublic();
        }
    }
}