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
}
