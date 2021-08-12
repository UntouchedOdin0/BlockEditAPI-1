package me.byteful.lib.blockedit.handlers.v1_8;

import lombok.RequiredArgsConstructor;
import me.byteful.lib.blockedit.BlockEditOption;
import me.byteful.lib.blockedit.Implementation;
import me.byteful.lib.blockedit.data.BlockLocation;
import net.minecraft.server.v1_8_R2.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.util.CraftMagicNumbers;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class NMS_v_1_8_R2_Handler implements Implementation {
  private final BlockEditOption option;

  @Override
  public void setBlock(
      @NotNull BlockLocation location,
      @NotNull Material material,
      @Nullable MaterialData data,
      boolean applyPhysics) {
    final World world = ((CraftWorld) location.getWorld()).getHandle();
    final BlockPosition bp = new BlockPosition(location.getX(), location.getY(), location.getZ());
    final IBlockData bd =
        data == null
            ? CraftMagicNumbers.getBlock(material).getBlockData()
            : Block.getByCombinedId(material.getId() + (data.getData() << 12));

    if (option == BlockEditOption.NMS_SAFE) {
      world.setTypeAndData(bp, bd, applyPhysics ? 3 : 2);
    } else if (option == BlockEditOption.NMS_FAST) {
      if (!location.getChunk().isLoaded()) {
        location.getChunk().load(true);
      }

      final Chunk chunk = world.getChunkAt(location.getX() >> 4, location.getZ() >> 4);
      chunk.a(bp, bd);
      if (applyPhysics) {
        world.update(bp, chunk.getType(bp));
      }

      world.notify(bp);
    }
  }
}
