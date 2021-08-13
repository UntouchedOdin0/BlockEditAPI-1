package me.byteful.lib.blockedit.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

/** A simple class to easily handle immutable block locations. */
@Getter
@EqualsAndHashCode
@ToString
public final class BlockLocation {
  @NotNull
  private final String world;
  private final int x, y, z;

  public BlockLocation(@NotNull final String world, final int x, final int y, final int z) {
    this.world = world;
    this.x = x;
    this.y = Math.min(Bukkit.getWorld(world).getMaxHeight(), y);
    this.z = z;
  }

  @NotNull
  public static BlockLocation of(@NotNull final String world, final int x, final int y, final int z) {
    return new BlockLocation(world, x, y, z);
  }

  @NotNull
  public static BlockLocation fromLocation(@NotNull final Location location) {
    return new BlockLocation(
        location.getWorld().getName(),
        location.getBlockX(),
        location.getBlockY(),
        location.getBlockZ());
  }

  @NotNull
  public static BlockLocation fromBlock(@NotNull final Block block) {
    return fromLocation(block.getLocation());
  }

  @NotNull
  public Block getBlock() {
    return getLocation().getBlock();
  }

  @NotNull
  public Location getLocation() {
    return new Location(Bukkit.getWorld(world), x, y, z);
  }

  @NotNull
  public World getWorld() {
    return Bukkit.getWorld(world);
  }

  @NotNull
  public Chunk getChunk() {
    return getLocation().getChunk();
  }
}
