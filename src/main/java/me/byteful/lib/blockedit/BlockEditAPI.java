package me.byteful.lib.blockedit;

import lombok.experimental.UtilityClass;
import me.byteful.lib.blockedit.data.BlockLocation;
import me.byteful.lib.blockedit.handlers.other.BukkitHandler;
import me.byteful.lib.blockedit.handlers.v1_10.NMS_v_1_10_R1_Handler;
import me.byteful.lib.blockedit.handlers.v1_11.NMS_v_1_11_R1_Handler;
import me.byteful.lib.blockedit.handlers.v1_12.NMS_v_1_12_R1_Handler;
import me.byteful.lib.blockedit.handlers.v1_13.NMS_v_1_13_R1_Handler;
import me.byteful.lib.blockedit.handlers.v1_13.NMS_v_1_13_R2_Handler;
import me.byteful.lib.blockedit.handlers.v1_14.NMS_v_1_14_R1_Handler;
import me.byteful.lib.blockedit.handlers.v1_15.NMS_v_1_15_R1_Handler;
import me.byteful.lib.blockedit.handlers.v1_16.NMS_v_1_16_R1_Handler;
import me.byteful.lib.blockedit.handlers.v1_16.NMS_v_1_16_R2_Handler;
import me.byteful.lib.blockedit.handlers.v1_16.NMS_v_1_16_R3_Handler;
import me.byteful.lib.blockedit.handlers.v1_17.NMS_v_1_17_R1_Handler;
import me.byteful.lib.blockedit.handlers.v1_8.NMS_v_1_8_R1_Handler;
import me.byteful.lib.blockedit.handlers.v1_8.NMS_v_1_8_R2_Handler;
import me.byteful.lib.blockedit.handlers.v1_8.NMS_v_1_8_R3_Handler;
import me.byteful.lib.blockedit.handlers.v1_9.NMS_v_1_9_R1_Handler;
import me.byteful.lib.blockedit.handlers.v1_9.NMS_v_1_9_R2_Handler;
import me.byteful.lib.blockedit.region.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The core class of BlockEditAPI. Contains all the methods needed to initialize and use
 * BlockEditAPI.
 */
@UtilityClass
public final class BlockEditAPI {
  private static int version;
  private static Implementation impl;

  /**
   * Initializes BlockEditAPI and auto-detects the Implementation to be used (affected by
   * BlockEditOption).
   *
   * @param option the options used to determine the implementation
   */
  public static void initialize(@NotNull final BlockEditOption option) {
    if (BlockEditAPI.impl != null) {
      System.out.println(
          "[WARNING] BlockEditAPI is already initialized! This can be caused because BlockEditAPI was not relocated and another instance has already initialized.");
      System.out.println("BlockEditAPI will override the last initialization data.");
    }

    final String[] split = Bukkit.getVersion().split(" ");
    BlockEditAPI.version =
        Integer.parseInt(split[split.length - 1].trim().replace(")", "").split("\\.")[1]);

    if (option == BlockEditOption.BUKKIT) {
      BlockEditAPI.impl = new BukkitHandler();
    } else {
      final String version =
          Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);
      BlockEditAPI.impl = fromNMSVersion(option, version);

      if (!(impl instanceof BukkitHandler)) {
        System.out.println("[BlockEditAPI] Found NMS version: v" + version);

        return;
      }
    }

    System.out.println("[BlockEditAPI] Using built-in Bukkit methods...");
  }

  /**
   * Initializes BlockEditAPI and uses provided implementation.
   *
   * <p>Note: This is not recommended! It's best to use the implementations provided by BlockEditAPI
   * and let it auto-detect the best one.
   *
   * @param implementation the implementation to use
   */
  public static void initialize(@NotNull final Implementation implementation) {
    if (BlockEditAPI.impl != null) {
      System.out.println(
          "[WARNING] BlockEditAPI is already initialized! This can be caused because BlockEditAPI was not relocated and another instance has already initialized.");
      System.out.println("BlockEditAPI will override the last initialization data.");
    }

    final String[] split = Bukkit.getVersion().split(" ");
    BlockEditAPI.version =
        Integer.parseInt(split[split.length - 1].trim().replace(")", "").split("\\.")[1]);

    BlockEditAPI.impl = implementation;
  }

  private static Implementation fromNMSVersion(
      @NotNull final BlockEditOption option, @NotNull final String version) {
    switch (version) {
      case "1_8_R1":
        return new NMS_v_1_8_R1_Handler(option);
      case "1_8_R2":
        return new NMS_v_1_8_R2_Handler(option);
      case "1_8_R3":
        return new NMS_v_1_8_R3_Handler(option);
      case "1_9_R1":
        return new NMS_v_1_9_R1_Handler(option);
      case "1_9_R2":
        return new NMS_v_1_9_R2_Handler(option);
      case "1_10_R1":
        return new NMS_v_1_10_R1_Handler(option);
      case "1_11_R1":
        return new NMS_v_1_11_R1_Handler(option);
      case "1_12_R1":
        return new NMS_v_1_12_R1_Handler(option);
      case "1_13_R1":
        return new NMS_v_1_13_R1_Handler(option);
      case "1_13_R2":
        return new NMS_v_1_13_R2_Handler(option);
      case "1_14_R1":
        return new NMS_v_1_14_R1_Handler(option);
      case "1_15_R1":
        return new NMS_v_1_15_R1_Handler(option);
      case "1_16_R1":
        return new NMS_v_1_16_R1_Handler(option);
      case "1_16_R2":
        return new NMS_v_1_16_R2_Handler(option);
      case "1_16_R3":
        return new NMS_v_1_16_R3_Handler(option);
      case "1_17_R1":
        return new NMS_v_1_17_R1_Handler(option);
      default:
        {
          System.out.println(
              "[BlockEditAPI] Failed to find implementation for server's NMS version. Defaulted to Bukkit methods. Either set an implementation yourself or use the default Bukkit option.");

          return new BukkitHandler();
        }
    }
  }

  private static void checkForInitialization() {
    if (impl == null) {
      throw new IllegalStateException("Please initialize BlockEditAPI before using it!");
    }
  }

  // ======================================================================================================
  // ======================================================================================================
  // ======================================================================================================
  // ======================================================================================================
  // ======================================================================================================
  // ======================================================================================================

  /**
   * Sets the block's type and data.
   *
   * @param block the block to set the type and data
   * @param material the type to use
   * @param data the data for the type, set to null on 1.13+ (will automatically set to null if
   *     1.13+ is detected)
   * @param applyPhysics true if physics should be calculated (will slow the operation down)
   */
  public static void setBlock(
      @NotNull final Block block,
      @NotNull final Material material,
      @Nullable final MaterialData data,
      boolean applyPhysics) {
    checkForInitialization();

    if(!material.isBlock()) {
      throw new IllegalArgumentException("Material (" + material.name() + ") is not a block material!");
    }

    if (version >= 13) {
      impl.setBlock(BlockLocation.fromBlock(block), material, null, applyPhysics);
    } else {
      impl.setBlock(BlockLocation.fromBlock(block), material, data, applyPhysics);
    }
  }

  /**
   * Sets the location's block's type and data.
   *
   * @param block the block's location
   * @param material the type to use
   * @param data the data for the type, set to null on 1.13+ (will automatically set to null if
   *     1.13+ is detected)
   * @param applyPhysics true if physics should be calculated (will slow the operation down)
   */
  public static void setBlock(
      @NotNull final Location block,
      @NotNull final Material material,
      @Nullable final MaterialData data,
      boolean applyPhysics) {
    checkForInitialization();

    if(!material.isBlock()) {
      throw new IllegalArgumentException("Material (" + material.name() + ") is not a block material!");
    }

    if (version >= 13) {
      impl.setBlock(BlockLocation.fromLocation(block), material, null, applyPhysics);
    } else {
      impl.setBlock(BlockLocation.fromLocation(block), material, data, applyPhysics);
    }
  }

  /**
   * Sets the location's block's type and data.
   *
   * @param block the block's location
   * @param material the type to use
   * @param data the data for the type, set to null on 1.13+ (will automatically set to null if
   *     1.13+ is detected)
   * @param applyPhysics true if physics should be calculated (will slow the operation down)
   */
  public static void setBlock(
      @NotNull final BlockLocation block,
      @NotNull final Material material,
      @Nullable final MaterialData data,
      boolean applyPhysics) {
    checkForInitialization();

    if(!material.isBlock()) {
      throw new IllegalArgumentException("Material (" + material.name() + ") is not a block material!");
    }

    if (version >= 13) {
      impl.setBlock(block, material, null, applyPhysics);
    } else {
      impl.setBlock(block, material, data, applyPhysics);
    }
  }

  /**
   * Iterates through all blocks and performs the set type and data action.
   *
   * @param blocks the blocks to iterate through
   * @param material the type to use
   * @param data the data for the type, set to null on 1.13+ (will automatically set to null if
   *     1.13+ is detected)
   * @param applyPhysics true if physics should be calculated (will slow the operation down)
   */
  public static void setBlocks(
      @NotNull final List<BlockLocation> blocks,
      @NotNull final Material material,
      @Nullable final MaterialData data,
      boolean applyPhysics) {
    checkForInitialization();

    blocks.forEach(loc -> setBlock(loc, material, data, applyPhysics));
  }

  /**
   * Sets all blocks in provided region to provided type and data.
   *
   * @param region the region to set blocks in
   * @param material the type to use
   * @param data the data for the type, set to null on 1.13+ (will automatically set to null if
   *     1.13+ is detected)
   * @param applyPhysics true if physics should be calculated (will slow the operation down)
   */
  public static void setBlocks(
      @NotNull final Region region,
      @NotNull final Material material,
      @Nullable final MaterialData data,
      boolean applyPhysics) {
    checkForInitialization();

    for (Block block : region) {
      setBlock(block, material, data, applyPhysics);
    }
  }

  /**
   * Sets all blocks in provided region to a random type and data from the list(s) provided.
   *
   * @param region the region to set blocks in
   * @param materials the types to use (randomly selected for each block)
   * @param data the data for the type (randomly selected with same index as material), set to null
   *     on 1.13+ (will automatically set to null if 1.13+ is detected)
   * @param applyPhysics true if physics should be calculated (will slow the operation down)
   */
  public static void setBlocksRandomType(
      @NotNull final Region region,
      @NotNull final List<Material> materials,
      @Nullable final List<MaterialData> data,
      boolean applyPhysics) {
    checkForInitialization();

    if (data != null && materials.size() != data.size()) {
      throw new IllegalArgumentException("Materials and data lists have to be the same size!");
    }

    for (Block block : region) {
      final int rand = ThreadLocalRandom.current().nextInt(materials.size());
      final Material material = materials.get(rand);
      final MaterialData materialData = data != null ? data.get(rand) : null;
      setBlock(block, material, materialData, applyPhysics);
    }
  }
}
