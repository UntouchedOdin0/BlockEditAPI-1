package me.byteful.lib.blockedit;

/**
 * Options to configure the method that BlockEditAPI will use to modify blocks.
 *
 * <p>Note: All "blocks per second" calculations here were tested on an AMD Ryzen 3600X with 3200
 * MHZ DDR4 16 GB RAM. The MC server was running PaperSpigot 1.8.8 with 4 GB of allocated RAM.
 */
public enum BlockEditOption {
  /**
   * Uses NMSWorld to set the block type and data. This method can place 80k-90k blocks per second.
   *
   * <p>Note: This is the only NMS method that performs light updates and sends the changed data to
   * players who currently see the chunk.
   */
  NMS_SAFE,
  /**
   * Uses NMSChunk to set the block type and data. This method can place around 1.7-2.2 million
   * blocks per second.
   *
   * <p>Note: This method does not perform light updates. This method can also cause
   * incompatibilities.
   */
  NMS_FAST,
  /**
   * Uses NMSDataPalette to set the block type and data. This method can place around 12-14 million
   * blocks per second.
   *
   * <p>Note: This method is extremely fast, but may fail to set blocks. The chunks will also be
   * forcefully reloaded which can cause incompatibilities with certain plugins.
   */
  NMS_UNSAFE,
  /**
   * Uses built-in Bukkit methods to edit blocks. This method can place 50k-60k blocks per second.
   *
   * <p>Note: This is very slow and uses a lot of resources, but it's the safest and most compatible
   * method.
   */
  BUKKIT;
}
