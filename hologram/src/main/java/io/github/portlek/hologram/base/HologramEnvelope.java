package io.github.portlek.hologram.base;

import io.github.portlek.hologram.api.Hologram;
import io.github.portlek.hologram.api.IHologram;
import io.github.portlek.hologram.api.MckNMSHologram;
import io.github.portlek.hologram.nms.v1_10_R1.Hologram1_10_R1;
import io.github.portlek.hologram.nms.v1_11_R1.Hologram1_11_R1;
import io.github.portlek.hologram.nms.v1_12_R1.Hologram1_12_R1;
import io.github.portlek.hologram.nms.v1_13_R1.Hologram1_13_R1;
import io.github.portlek.hologram.nms.v1_13_R2.Hologram1_13_R2;
import io.github.portlek.hologram.nms.v1_14_R1.Hologram1_14_R1;
import io.github.portlek.hologram.nms.v1_8_R1.Hologram1_8_R1;
import io.github.portlek.hologram.nms.v1_8_R2.Hologram1_8_R2;
import io.github.portlek.hologram.nms.v1_8_R3.Hologram1_8_R3;
import io.github.portlek.hologram.nms.v1_9_R1.Hologram1_9_R1;
import io.github.portlek.hologram.nms.v1_9_R2.Hologram1_9_R2;
import io.github.portlek.itemstack.Colored;
import io.github.portlek.location.StringOf;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.versionmatched.VersionMatched;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class HologramEnvelope implements Hologram {

    private static final IHologram HOLOGRAM = new VersionMatched<>(
        new MckNMSHologram(),
        Hologram1_8_R1.class,
        Hologram1_8_R2.class,
        Hologram1_8_R3.class,
        Hologram1_9_R1.class,
        Hologram1_9_R2.class,
        Hologram1_10_R1.class,
        Hologram1_11_R1.class,
        Hologram1_12_R1.class,
        Hologram1_13_R1.class,
        Hologram1_13_R2.class,
        Hologram1_14_R1.class
    ).of().instance();

    private static final double OFFSET = 0.23D;

    @NotNull
    private final Location location;

    @NotNull
    private final List<String> lines;

    @NotNull
    private final World world;

    @NotNull
    private final List<Integer> ids = new ArrayList<>();

    @NotNull
    private final List<Object> entities = new ArrayList<>();

    public HologramEnvelope(@NotNull Location location, @NotNull List<String> lines) {
        this.location = location;
        this.lines = new ArrayList<>(lines);
        world = Objects.requireNonNull(this.location.getWorld());
        update();
    }

    @Override
    public void displayTo(@NotNull Player... player) {
        Location current = location.clone().add(0.0D, OFFSET * lines.size() - 1.97D, 0.0D);
        for (String str : lines) {
            Object[] packet = HOLOGRAM.createPacket(current, new Colored(str).value());
            ids.add((Integer)packet[1]);

            for (Player p : player) {
                HOLOGRAM.sendPacket(p, packet[0]);
            }

            current.subtract(0.0D, OFFSET, 0.0D);
        }
    }

    @Override
    public void removeFrom(@NotNull Player... player) {
        Object packet = null;

        for (Integer id : ids) {
            packet = HOLOGRAM.removePacket(id);
        }

        if (packet != null)
            for (Player p : player) {
                HOLOGRAM.sendPacket(p, packet);
            }
    }

    @Override
    public void removeLines() {
        lines.clear();
        update();
    }

    @Override
    public void addLine(@NotNull String... line) {
        addLines(
            new ListOf<>(
                line
            )
        );
    }

    @Override
    public void addLines(@NotNull List<String> lines) {
        this.lines.addAll(lines);
        update();
    }

    @Override
    public void setLines(@NotNull List<String> lines) {
        removeLines();
        addLines(lines);
    }

    @Override
    public void spawn() {
        Location current = location.clone()
            .add(0.0D, OFFSET * lines.size() - 1.97D, 0.0D)
            .add(0.0D, OFFSET, 0.0D);

        for (String line : lines) {
            entities.add(HOLOGRAM.spawnHologram(line, current.subtract(0.0D, OFFSET, 0.0D)));
        }
    }

    @Override
    public void save(@NotNull IYaml file, @NotNull UUID uuid) {
        file.set("Holograms." + uuid.toString() + ".location", new StringOf(location).asString());
        file.set("Holograms." + uuid.toString() + ".lines", lines);
    }

    @Override
    public void remove() {
        for (Object entity : entities) {
            HOLOGRAM.removeHologram(world, entity);
        }
    }

    private void update() {
        if (entities.isEmpty())
            return;

        for (Object ent : entities) {
            HOLOGRAM.removeHologram(world, ent);
        }

        Location current = location.clone()
            .add(0.0D, OFFSET * lines.size() - 1.97D, 0.0D);

        for (int b = 0, j = lines.size(); b < j; b++) {
            String text = new Colored(lines.get(b)).toString();

            if (b >= entities.size())
                HOLOGRAM.spawnHologram(text, current);
            else
                HOLOGRAM.configureHologram(entities.get(b), text, current);

            current.subtract(0.0D, OFFSET, 0.0D);
        }
    }

}
