package io.github.portlek.hologram.nms.v1_14_R1;

import io.github.portlek.hologram.api.IHologram;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Hologram1_14_R1 implements IHologram {

    @Override
    @NotNull
    public Object spawnHologram(@NotNull String text, @NotNull Location location) {
        WorldServer world = ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle();
        EntityArmorStand armorStand = new EntityArmorStand(world, location.getX(), location.getY(), location.getZ());
        configureHologram(armorStand, text, location);
        world.addEntity(armorStand, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return armorStand;
    }

    @Override
    public void removeHologram(@NotNull org.bukkit.World world, @NotNull Object entity) {
        WorldServer nmsWorld = ((CraftWorld)world).getHandle();
        nmsWorld.removeEntity((Entity)entity);
    }

    @Override
    @NotNull
    public Object[] createPacket(@NotNull Location location, @NotNull String text) {
        WorldServer world = ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle();
        EntityArmorStand armorStand = new EntityArmorStand(world, location.getX(), location.getY(), location.getZ());
        configureHologram(armorStand, text, location);
        return new Object[] {
            new PacketPlayOutSpawnEntityLiving(armorStand),
            armorStand.getId()
        };
    }

    @Override
    @NotNull
    public Object removePacket(int id) {
        return new PacketPlayOutEntityDestroy(id);
    }

    @Override
    public void configureHologram(@NotNull Object entity, @NotNull String text, @NotNull Location location) {
        EntityArmorStand nmsEntity = (EntityArmorStand)entity;
        nmsEntity.setCustomName(IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + text + "\"}"));
        nmsEntity.setCustomNameVisible(true);
        nmsEntity.setNoGravity(true);
        nmsEntity.setLocation(location.getX(), location.getY(), location.getZ(), 0.0F, 0.0F);
        nmsEntity.setInvisible(true);
    }

    @Override
    public void sendPacket(@NotNull Player player, @NotNull Object object) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet<?>)object);
    }

}
