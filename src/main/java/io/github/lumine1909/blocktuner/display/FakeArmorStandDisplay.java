package io.github.lumine1909.blocktuner.display;

import io.github.lumine1909.blocktuner.proxy.EntityProxy;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FakeArmorStandDisplay {

    private static final int ENTITY_ID = 1145141919;
    private static final UUID ENTITY_UUID = new UUID(114514, 1919810);

    public static void addArmorDisplay(Player player, Location location, Component name) {
        ServerPlayer sp = ((CraftPlayer) player).getHandle();
        sp.connection.send(new ClientboundAddEntityPacket(
            ENTITY_ID, ENTITY_UUID,
            location.x(), location.y(), location.z(),
            0, 0,
            EntityType.ARMOR_STAND, 0, Vec3.ZERO, 0
        ));
        sp.connection.send(new ClientboundSetEntityDataPacket(ENTITY_ID, List.of(
            SynchedEntityData.DataValue.create(EntityProxy.staticAccess.sharedFlagsId(), (byte) 32),
            SynchedEntityData.DataValue.create(EntityProxy.staticAccess.customName(), Optional.of(PaperAdventure.asVanilla(name))),
            SynchedEntityData.DataValue.create(EntityProxy.staticAccess.customNameVisible(), true),
            SynchedEntityData.DataValue.create(EntityProxy.staticAccess.noGravity(), true),
            SynchedEntityData.DataValue.create(ArmorStand.DATA_CLIENT_FLAGS, (byte) 21)
        )));
    }

    public static void updateArmorDisplay(Player player, Component name) {
        ServerPlayer sp = ((CraftPlayer) player).getHandle();
        sp.connection.send(new ClientboundSetEntityDataPacket(ENTITY_ID, List.of(
            SynchedEntityData.DataValue.create(EntityProxy.staticAccess.customName(), Optional.of(PaperAdventure.asVanilla(name)))
        )));
    }

    public static void deleteArmorDisplay(Player player) {
        ServerPlayer sp = ((CraftPlayer) player).getHandle();
        sp.connection.send(new ClientboundRemoveEntitiesPacket(ENTITY_ID));
    }
}