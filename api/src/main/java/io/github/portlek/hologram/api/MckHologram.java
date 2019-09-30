package io.github.portlek.hologram.api;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MckHologram implements Hologram {
    @NotNull
    @Override
    public Object spawnHologram(@NotNull String paramString, @NotNull Location paramLocation) {
        return "";
    }
    @Override
    public void removeHologram(@NotNull World paramWorld, @NotNull Object paramObject) {
    }
    @NotNull
    @Override
    public Object[] createPacket(@NotNull Location paramLocation, @NotNull String paramString) {
        return new Object[0];
    }
    @NotNull
    @Override
    public Object removePacket(int paramInt) {
        return "";
    }
    @Override
    public void configureHologram(@NotNull Object paramObject, @NotNull String paramString, @NotNull Location paramLocation) {
    }
    @Override
    public void sendPacket(@NotNull Player player, @NotNull Object object) {
    }
}
