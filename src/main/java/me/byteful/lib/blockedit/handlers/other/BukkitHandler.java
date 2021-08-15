package me.byteful.lib.blockedit.handlers.other;

import me.byteful.lib.blockedit.Implementation;
import me.byteful.lib.blockedit.data.BlockLocation;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BukkitHandler implements Implementation {
  @Override
  public void setBlock(
      @NotNull BlockLocation location,
      @NotNull Material material,
      @Nullable MaterialData data,
      boolean applyPhysics) {
    final BlockState state = location.getBlock().getState();
    state.setType(material);
    if (data != null) {
      state.setData(data);
    }
    state.update(true, applyPhysics);
  }
}
