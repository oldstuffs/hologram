package io.github.portlek.hologram.api;

import io.github.portlek.mcyaml.IYaml;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public interface Hologram {

    void displayTo(@NotNull final Player... players);

    void removeFrom(@NotNull final Player... players);

    void spawn();

    void remove();

    void save(@NotNull final IYaml yaml, @NotNull final UUID uuid);

    void removeLines();

    void addLine(@NotNull final String... lines);

    void addLines(@NotNull final List<String> lines);

    void setLines(@NotNull final List<String> lines);

}
