package io.github.portlek.hologram.api;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Hologram {

    @NotNull
    Object spawnHologram(@NotNull String paramString, @NotNull Location paramLocation);

    void removeHologram(@NotNull World paramWorld, @NotNull Object paramObject);

    @NotNull
    Object[] createPacket(@NotNull Location paramLocation, @NotNull String paramString);

    @NotNull
    Object removePacket(int paramInt);

    void configureHologram(@NotNull Object paramObject, @NotNull String paramString, @NotNull Location paramLocation);

    void sendPacket(@NotNull Player player, @NotNull Object object);

}
