package io.github.portlek.hologram.base;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class Holograms implements Listener {

    @NotNull
    private final Plugin plugin;

    public Holograms(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }



}
