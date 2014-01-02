package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.command.SaveCommand;
import java.util.*;
import java.util.Map.Entry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class BlueprintHandler {

	//borrowed from VoxelSniper's Undo.java
	private static final Set<Material> FALLOFF_MATERIALS = EnumSet.of(
			Material.REDSTONE_WIRE,
			Material.REDSTONE_TORCH_OFF,
			Material.REDSTONE_TORCH_ON,
			Material.DIODE_BLOCK_OFF,
			Material.DIODE_BLOCK_ON,
			Material.LEVER,
			Material.WATER,
			Material.STATIONARY_WATER,
			Material.LAVA,
			Material.STATIONARY_LAVA,
			Material.SAPLING,
			Material.BED_BLOCK,
			Material.POWERED_RAIL,
			Material.DETECTOR_RAIL,
			Material.LONG_GRASS,
			Material.DEAD_BUSH,
			Material.PISTON_EXTENSION,
			Material.YELLOW_FLOWER,
			Material.RED_ROSE,
			Material.BROWN_MUSHROOM,
			Material.RED_MUSHROOM,
			Material.TORCH,
			Material.FIRE,
			Material.CROPS,
			Material.SIGN_POST,
			Material.WOODEN_DOOR,
			Material.LADDER,
			Material.RAILS,
			Material.WALL_SIGN,
			Material.STONE_PLATE,
			Material.IRON_DOOR_BLOCK,
			Material.WOOD_PLATE,
			Material.STONE_BUTTON,
			Material.SNOW,
			Material.CACTUS,
			Material.SUGAR_CANE_BLOCK,
			Material.CAKE_BLOCK,
			Material.TRAP_DOOR,
			Material.PUMPKIN_STEM,
			Material.MELON_STEM,
			Material.VINE,
			Material.WATER_LILY,
			Material.NETHER_WARTS
	);

	final HashMap<Location, ComboBlock> blocks;
	final StampMode mode;

	/**
	 * Handles any exceptions. Must be called within a catch block where an exception is avaliable.
	 *
	 * @param e The exception thrown by the Blueprint
	 * @param book The book to save the exception onto
	 */
	public static void handleException(Exception e, ItemStack book) {
		String message = e.getMessage() + " (" + e.getClass().getSimpleName() + ")";
		if (book != null) {
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("AN ERROR OCCURED:");
			lore.add(message);
			book.getItemMeta().setLore(lore);
		}
	}

	public BlueprintHandler(ItemStack book) throws Exception {
		ItemMeta meta = book.getItemMeta();
		if (!(meta instanceof BookMeta)) throw new Exception("Can only use Written Books with Blueprints.");
		ArrayList<String> lines = getLines((BookMeta) meta);
		blocks = getBlocks(lines);
		mode = getMode(lines);
	}

	private ArrayList<String> getLines(BookMeta meta) {
		ArrayList<String> lines = new ArrayList<String>();
		for (String page : meta.getPages()) {
			lines.addAll(Arrays.asList(page.split("\n")));
		}
		return lines;
	}

	private StampMode getMode(ArrayList<String> lines) throws Exception {
		String modeLine = lines.get(3);
		String mode = modeLine.substring(6);
		try {
			StampMode stampMode = StampMode.valueOf(mode.toUpperCase());
			return stampMode;
		} catch (IllegalArgumentException e) {
			throw new Exception("'" + mode + "' is not a valid Stamp Mode (NONE, AIR, ALL)");
		}
	}

	private HashMap<Location, ComboBlock> getBlocks(ArrayList<String> lines) throws Exception {
		String[] dim = lines.get(2).substring(4).split("x");
		int dimX = Integer.parseInt(dim[0]);
		int dimY = Integer.parseInt(dim[1]);
		int dimZ = Integer.parseInt(dim[2]);
		if (dimX * dimY * dimZ > SaveCommand.MAX_CUBOID_SIZE) {
			throw new Exception("Cuboid size (" + (dimX * dimY * dimZ) + ") is not allowed. Maxium allowed size is " + SaveCommand.MAX_CUBOID_SIZE);
		}
		HashMap<Location, ComboBlock> blocksMap = new HashMap<>();
		boolean blueprintStarted = false;
		for (String line : lines) {
			if (!blueprintStarted) {
				if (line.equals("*BLUEPRINT BEGIN")) blueprintStarted = true;
			} else {
				if (line.equals("*BLUEPRINT COMPLETE")) break;
				String[] parts = line.split(">");
				String[] relatives = parts[0].split(",");
				int relativeY = Integer.parseInt(relatives[0]);
				int relativeZ = Integer.parseInt(relatives[1]);
				String[] dataX = parts[1].split(";");
				for (int relativeX = 0; relativeX < dataX.length; relativeX++) {
					String data = dataX[relativeX];
					ComboBlock combo;
					String[] split = data.split(":");
					if (split.length == 2) combo = new ComboBlock(Integer.parseInt(split[0]), Byte.parseByte(split[1]));
					else combo = new ComboBlock(Integer.parseInt(split[0]));
					blocksMap.put(new Location(null, relativeX, relativeY, relativeZ), combo);
				}
			}
		}
		return blocksMap;
	}

	public boolean checkIfExists(Location origin) {
		int existsCount = 0;
		int totalBlocks = 0;
		for (Entry<Location, ComboBlock> entry : blocks.entrySet()) {
			Location relative = entry.getKey();
			Block existing = origin.clone().add(relative.getBlockX(), relative.getBlockY(), relative.getBlockZ()).getBlock();
			if (mode.stamp.countAsBlockFromBlueprint(existing, entry.getValue())) {
				totalBlocks += 1;
				if (entry.getValue().equals(existing)) existsCount += 1;
			}
		}
		//System.out.println(existsCount + " exists out of " + totalBlocks + " relevant blocks.");

		return totalBlocks == 0 ? false : existsCount > (totalBlocks - (totalBlocks / 4));
	}

	public void remove(Location origin) {
		for (Entry<Location, ComboBlock> entry : blocks.entrySet()) {
			Location relative = entry.getKey();
			setBlock(origin.clone().add(relative.getBlockX(), relative.getBlockY(), relative.getBlockZ()).getBlock(), new ComboBlock(0), entry.getValue());
		}
	}

	public void paste(Location origin) {
		setLast.clear();
		for (Entry<Location, ComboBlock> entry : blocks.entrySet()) {
			Location relative = entry.getKey();
			setBlock(origin.clone().add(relative.getBlockX(), relative.getBlockY(), relative.getBlockZ()).getBlock(), entry.getValue(), entry.getValue());
		}
		doSetLast();
	}

	private final HashMap<Block, ComboBlock> setLast = new HashMap<>();

	private void setBlock(Block existing, ComboBlock newBlock, ComboBlock blueprintBlock) {
		if (newBlock.getID() != 0) {
			if (FALLOFF_MATERIALS.contains(Material.getMaterial(newBlock.getID()))) {
				setLast.put(existing, newBlock);
				return;
			}
		}
		mode.stamp.setBlock(existing, newBlock, blueprintBlock);
	}

	private void doSetLast() {
		for (Entry<Block, ComboBlock> entry : setLast.entrySet()) {
			mode.stamp.setBlock(entry.getKey(), entry.getValue(), entry.getValue());
		}
	}

	//Stamp Modes
	public enum StampMode {

		NONE(new AbstractStampMode() {
			@Override
			public void setBlock(Block block, ComboBlock newBlock, ComboBlock blueprintBlock) {
				if (blueprintBlock.getID() != 0 && countAsBlockFromBlueprint(block, blueprintBlock)) {
					block.setTypeId(0, false);
					block.setData((byte) 0, false);
				} else if (block.getType() == Material.AIR) {
					block.setTypeId(newBlock.getID(), false);
					block.setData(newBlock.getData(), false);
				}
			}

			@Override
			public boolean countAsBlockFromBlueprint(Block block, ComboBlock blueprintBlock) {
				return block.getType() == Material.AIR ? false : blueprintBlock.equals(new ComboBlock(block));
			}
		}),
		ALL(new AbstractStampMode() {
			@Override
			public void setBlock(Block block, ComboBlock newBlock, ComboBlock blueprintBlock) {
				block.setTypeId(newBlock.getID(), false);
				block.setData(newBlock.getData(), false);
			}

			@Override
			public boolean countAsBlockFromBlueprint(Block block, ComboBlock blueprintBlock) {
				return !(block.getTypeId() == 0 && blueprintBlock.getID() == 0);
			}
		}),
		AIR(new AbstractStampMode() {
			@Override
			public void setBlock(Block block, ComboBlock newBlock, ComboBlock blueprintBlock) {
				if (newBlock.getID() != 0) {
					block.setTypeId(newBlock.getID(), false);
					block.setData(newBlock.getData(), false);
				} else if (blueprintBlock.equals(block)) {
					block.setTypeId(0, false);
					block.setData((byte) 0, false);
				}
			}

			@Override
			public boolean countAsBlockFromBlueprint(Block block, ComboBlock blueprintBlock) {
				return blueprintBlock.getID() != 0;
			}
		});

		protected final AbstractStampMode stamp;

		private StampMode(AbstractStampMode stamp) {
			this.stamp = stamp;
		}
	}

}
