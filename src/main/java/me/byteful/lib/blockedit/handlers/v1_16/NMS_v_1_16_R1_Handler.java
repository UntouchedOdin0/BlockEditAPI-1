package me.byteful.lib.blockedit.handlers.v1_16;

import lombok.RequiredArgsConstructor;
import me.byteful.lib.blockedit.BlockEditOption;
import me.byteful.lib.blockedit.Implementation;
import me.byteful.lib.blockedit.data.BlockLocation;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftMagicNumbers;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class NMS_v_1_16_R1_Handler implements Implementation {
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
      final IBlockData oldData = chunk.getType(bp);

      chunk.setType(bp, bd, applyPhysics);
      world.notify(bp, oldData, bd, applyPhysics ? 3 : 2);
    } else {
      throw new UnsupportedOperationException(
          "Specified option is not available for current implementation. (v1.16-R1)");
    }
  }
}
