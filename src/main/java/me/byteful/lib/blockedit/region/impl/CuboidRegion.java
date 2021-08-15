package me.byteful.lib.blockedit.region.impl;

import lombok.Data;
import me.byteful.lib.blockedit.data.BlockLocation;
import me.byteful.lib.blockedit.region.Region;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
public class CuboidRegion implements Region {
  private final String world;
  private final BlockLocation pos1, pos2;

  public CuboidRegion(@NotNull final BlockLocation pos1, @NotNull final BlockLocation pos2) {
    if (!pos1.getWorld().equals(pos2.getWorld())) {
      throw new IllegalArgumentException("Locations must have the same world!");
    }

    this.world = pos1.getWorld().getName();
    this.pos1 = pos1;
    this.pos2 = pos2;
  }

  @NotNull
  @Override
  public Iterator<Block> iterator() {
    return new CuboidIterator(
        world,
        Math.min(pos1.getX(), pos2.getX()),
        Math.min(pos1.getY(), pos2.getY()),
        Math.min(pos1.getZ(), pos2.getZ()),
        Math.max(pos1.getX(), pos2.getX()),
        Math.max(pos1.getY(), pos2.getY()),
        Math.max(pos1.getZ(), pos2.getZ()));
  }

  @Override
  protected CuboidRegion clone() {
    return new CuboidRegion(pos1, pos2);
  }

  @Override
  public List<Block> getBlocks() {
    final Iterator<Block> iter = this.iterator();
    final List<Block> list = new ArrayList<>();

    while(iter.hasNext()) {
      list.add(iter.next());
    }

    return list;
  }

  @Override
  public int getVolume() {
    return getSizeX() * getSizeY() * getSizeZ();
  }

  @Override
  public int getSizeX() {
    return (Math.max(pos1.getX(), pos2.getX()) - Math.min(pos1.getX(), pos2.getX())) + 1;
  }

  @Override
  public int getSizeY() {
    return (Math.max(pos1.getY(), pos2.getY()) - Math.min(pos1.getY(), pos2.getY())) + 1;
  }

  @Override
  public int getSizeZ() {
    return (Math.max(pos1.getZ(), pos2.getZ()) - Math.min(pos1.getZ(), pos2.getZ())) + 1;
  }

  @Data
  private static final class CuboidIterator implements Iterator<Block> {
    private final String world;
    private final int baseX, baseY, baseZ;
    private final int sizeX, sizeY, sizeZ;
    private int x, y, z;

    public CuboidIterator(
        @NotNull final String world, int x1, int y1, int z1, int x2, int y2, int z2) {
      this.world = world;
      this.baseX = x1;
      this.baseY = y1;
      this.baseZ = z1;
      this.sizeX = Math.abs(x2 - x1) + 1;
      this.sizeY = Math.abs(y2 - y1) + 1;
      this.sizeZ = Math.abs(z2 - z1) + 1;
      this.x = this.y = this.z = 0;
    }

    @Override
    public boolean hasNext() {
      return this.x < this.sizeX && this.y < this.sizeY && this.z < this.sizeZ;
    }

    @Override
    public Block next() {
      final Block b =
          Bukkit.getWorld(world)
              .getBlockAt(this.baseX + this.x, this.baseY + this.y, this.baseZ + this.z);
      if (++x >= this.sizeX) {
        this.x = 0;
        if (++this.y >= this.sizeY) {
          this.y = 0;
          ++this.z;
        }
      }

      return b;
    }

    @Override
    public void remove() {}
  }
}
