package me.byteful.lib.blockedit.data;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * A simple class to easily handle block locations.
 */
@Data(staticConstructor = "of")
public final class BlockLocation {
  private final String world;
  private final int x,y,z;

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

  @NotNull
  public static BlockLocation fromLocation(@NotNull final Location location) {
    return new BlockLocation(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
  }

  @NotNull
  public static BlockLocation fromBlock(@NotNull final Block block) {
    return fromLocation(block.getLocation());
  }
}
