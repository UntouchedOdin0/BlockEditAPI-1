package me.byteful.lib.blockedit.handlers.v1_16;

import lombok.RequiredArgsConstructor;
import me.byteful.lib.blockedit.BlockEditOption;
import me.byteful.lib.blockedit.Implementation;
import me.byteful.lib.blockedit.data.BlockLocation;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class NMS_v_1_16_R3_Handler implements Implementation {
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
    final Chunk chunk = world.getChunkAt(location.getX() >> 4, location.getZ() >> 4);
    final IBlockData oldData = chunk.getType(bp);
    if (!location.getChunk().isLoaded()) {
      location.getChunk().load(true);
    }

    if (option == BlockEditOption.NMS_SAFE) {
      world.setTypeAndData(bp, bd, applyPhysics ? 3 : 2);
    } else if (option == BlockEditOption.NMS_FAST) {
      chunk.setType(bp, bd, applyPhysics);
      world.notify(bp, oldData, bd, applyPhysics ? 3 : 2);
    } else if(option == BlockEditOption.NMS_UNSAFE) {
      ChunkSection cs = chunk.getSections()[location.getY() >> 4];

      if(cs == chunk.a() || cs == null) {
        cs = new ChunkSection(location.getY() >> 4 << 4);
        chunk.getSections()[location.getY() >> 4] = cs;
      }

      if(applyPhysics) {
        cs.getBlocks().setBlock(location.getX() & 15, location.getY() & 15, location.getZ() & 15, bd);
        world.notify(bp, oldData, bd, 3);
      } else {
        cs.getBlocks().b(location.getX() & 15, location.getY() & 15, location.getZ() & 15, bd);
        world.notify(bp, oldData, bd, 2);
      }
      cs.recalcBlockCounts();
    }  else {
      throw new UnsupportedOperationException(
          "Specified option is not available for current implementation. (v1.16-R3)");
    }
  }
}
