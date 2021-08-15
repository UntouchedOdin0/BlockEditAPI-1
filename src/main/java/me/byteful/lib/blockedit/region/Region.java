package me.byteful.lib.blockedit.region;

import org.bukkit.block.Block;

import java.util.List;

public interface Region extends Iterable<Block>, Cloneable {
  /**
   * Returns all the blocks inside the region.
   *
   * @return the blocks in the region
   */
  List<Block> getBlocks();

  /**
   * Returns the volume (number of blocks) in the region.
   *
   *  @return the number of blocks in the region
   */
  int getVolume();

  /**
   * Returns the size of the region on the X axis.
   *
   * @return size of region on x axis
   */
  int getSizeX();

  /**
   * Returns the size of the region on the Y axis.
   *
   * @return size of region on y axis
   */
  int getSizeY();

  /**
   * Returns the size of the region on the Z axis.
   *
   * @return size of region on z axis
   */
  int getSizeZ();
}
