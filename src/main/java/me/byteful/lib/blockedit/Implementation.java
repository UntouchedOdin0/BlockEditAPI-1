package me.byteful.lib.blockedit;

import me.byteful.lib.blockedit.data.BlockLocation;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** The main interface to implement all methods used by BlockEditAPI. */
public interface Implementation {
  void setBlock(
      @NotNull final BlockLocation location,
      @NotNull final Material material,
      @Nullable final MaterialData data,
      final boolean applyPhysics);
}
