package io.github.portlek.hologram.base;

import io.github.portlek.hologram.api.Hologram;
import io.github.portlek.hologram.api.MckHologram;
import io.github.portlek.location.LocationOf;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.mcyaml.mck.MckFileConfiguration;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class Holograms implements Listener {

    private final HashMap<UUID, Hologram> holograms = new HashMap<>();

    @NotNull
    private final Plugin plugin;

    @NotNull
    private final IYaml yaml;

    public Holograms(@NotNull Plugin plugin, @NotNull IYaml yaml) {
        this.plugin = plugin;
        this.yaml = yaml;
    }

    public void initHologram() {
        new ListenerBasic<>(PluginDisableEvent.class, event -> {
            if (!event.getPlugin().equals(plugin))
                return;

            holograms.values().forEach(Hologram::remove);
        }).register(plugin);

        reloadHolograms();
    }

    public void reloadHolograms() {
        ConfigurationSection section = yaml.getSection("Holograms");

        if (section instanceof MckFileConfiguration)
            section = yaml.createSection("Holograms");

        for (String id : section.getKeys(false)) {
            final List<String> lines = yaml.getStringList("Holograms." + id + ".lines");
            final Location location = new LocationOf(yaml.getString("Holograms." + id + ".location").orElse("")).value();
            final Hologram hologram = new HologramOf(location, lines);
            this.holograms.put(UUID.fromString(id), hologram);
            hologram.spawn();
        }
    }

    @NotNull
    public Hologram getHologram(@NotNull final UUID uuid) {
        return holograms.getOrDefault(uuid, new MckHologram());
    }

    @NotNull
    public Hologram createHologram(@NotNull final Location location, @NotNull final List<String> lines) {
        return createHologramWithId(UUID.randomUUID(), location, lines);
    }

    @NotNull
    public Hologram createHologramWithId(@NotNull final UUID uuid, @NotNull final Location location, @NotNull final List<String> lines) {
        final HologramOf hologram = new HologramOf(location, lines);
        holograms.put(uuid, hologram);
        saveHologram();
        return hologram;
    }

    public void removeHologram(@NotNull final UUID uuid) {
        yaml.set("Holograms." + uuid, null);
        if (holograms.containsKey(uuid)) {
            getHologram(uuid).remove();
        }
        holograms.remove(uuid);
        saveHologram();
    }

    public void removeHolograms() {
        yaml.set("Holograms", null);
        holograms.values().forEach(Hologram::remove);
        holograms.clear();
    }

    private void saveHologram() {
        holograms.forEach((uuid, hologram) -> hologram.save(yaml, uuid));
    }

}
