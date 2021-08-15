package me.byteful.lib.blockedit.handlers.v1_8;

import lombok.RequiredArgsConstructor;
import me.byteful.lib.blockedit.BlockEditOption;
import me.byteful.lib.blockedit.Implementation;
import me.byteful.lib.blockedit.data.BlockLocation;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.util.CraftMagicNumbers;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class NMS_v_1_8_R1_Handler implements Implementation {
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
    if (!location.getChunk().isLoaded()) {
      location.getChunk().load(true);
    }

    final Chunk chunk = world.getChunkAt(location.getX() >> 4, location.getZ() >> 4);

    if (option == BlockEditOption.NMS_SAFE) {
      world.setTypeAndData(bp, bd, applyPhysics ? 3 : 2);
    } else if (option == BlockEditOption.NMS_FAST) {
      chunk.a(bp, bd);
      if (applyPhysics) {
        world.update(bp, chunk.getType(bp));
      }

      world.notify(bp);
    } else if(option == BlockEditOption.NMS_UNSAFE) {
      ChunkSection cs = chunk.getSections()[location.getY() >> 4];

      try {
        if(cs == a(chunk) || cs == null) {
          cs = new ChunkSection(location.getY() >> 4 << 4, true);
          chunk.getSections()[location.getY() >> 4] = cs;
        }
      } catch (IllegalAccessException | NoSuchFieldException e) {
        throw new RuntimeException(e);
      }

      cs.setType(location.getX() & 15, location.getY() & 15, location.getZ() & 15, bd);
      if (applyPhysics) {
        world.update(bp, chunk.getType(bp));
      }

      world.notify(bp);
      cs.recalcBlockCounts();
    } else {
      throw new UnsupportedOperationException(
          "Specified option is not available for current implementation. (v1.8-R1)");
    }
  }

  @Nullable
  private ChunkSection a(Chunk chunk) throws IllegalAccessException, NoSuchFieldException {
    ChunkSection[] var0 = chunk.getSections();

    for(int var1 = var0.length - 1; var1 >= 0; --var1) {
      ChunkSection var2 = var0[var1];
      if (!(var2 == null || var2.a())) {
        return var2;
      }
    }

    return null;
  }
}
