package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import java.util.EnumSet;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.meta.BookMeta;

public class BlueprintModifier {

	//borrowed from VoxelSniper Undo.java
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

	public enum OverrideMode {

		NONE, AIR, ALL;
	}

	public boolean checkIfExists(Processor p) {
		BookMeta meta = (BookMeta) p.getDispensed().getItemMeta();
		String page = meta.getPage(1);
		String[] lines = page.split("\n");
		//retrieve dimensions
		String[] dim = lines[2].substring(4).split("x");
		int dimX = Integer.parseInt(dim[0]);
		int dimY = Integer.parseInt(dim[1]);
		int dimZ = Integer.parseInt(dim[2]);

		for (int y = 0; y < dimY; y++) {
			int start = 6 + (y * (dimZ + 2));
			for (int z = 0; z < dimZ; z++) {
				int line = start + z;
				String[] blocks = lines[line].split(";");
				for (int x = 0; x < dimX; x++) {
					String block = blocks[x];
					ComboBlock combo;
					String[] split = block.split(":");
					if (split.length == 2) combo = new ComboBlock(Integer.parseInt(split[0]), Byte.parseByte(split[1]));
					else combo = new ComboBlock(Integer.parseInt(split[0]));
					if (combo.getID() != 0 && this.blockExists(combo, x, y, z, p)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void remove(Processor p) {
		BookMeta meta = (BookMeta) p.getDispensed().getItemMeta();
		String page = meta.getPage(1);
		String[] lines = page.split("\n");
		//retrieve dimensions
		String[] dim = lines[2].substring(4).split("x");
		int dimX = Integer.parseInt(dim[0]);
		int dimY = Integer.parseInt(dim[1]);
		int dimZ = Integer.parseInt(dim[2]);

		for (int y = 0; y < dimY; y++) {
			for (int z = 0; z < dimZ; z++) {
				for (int x = 0; x < dimX; x++) {
					removeBlock(x, y, z, p, true);
				}
			}
		}
		for (int y = 0; y < dimY; y++) {
			for (int z = 0; z < dimZ; z++) {
				for (int x = 0; x < dimX; x++) {
					removeBlock(x, y, z, p, false);
				}
			}
		}
	}

	public void paste(Processor p) {
		BookMeta meta = (BookMeta) p.getDispensed().getItemMeta();
		String page = meta.getPage(1);
		String[] lines = page.split("\n");
		//retrieve dimensions
		String[] dim = lines[2].substring(4).split("x");
		int dimX = Integer.parseInt(dim[0]);
		int dimY = Integer.parseInt(dim[1]);
		int dimZ = Integer.parseInt(dim[2]);

		//retrieve OverrideMode
		OverrideMode mode = OverrideMode.valueOf(lines[3].substring(8));
		if (mode == null) mode = OverrideMode.ALL;

		loop(dimX, dimY, dimZ, lines, mode, p, false);
		loop(dimX, dimY, dimZ, lines, mode, p, true);

	}

	private void loop(int dimX, int dimY, int dimZ, String[] lines, OverrideMode mode, Processor p, boolean fall) {
		for (int y = 0; y < dimY; y++) {
			int start = 6 + (y * (dimZ + 2));
			for (int z = 0; z < dimZ; z++) {
				int line = start + z;
				String[] blocks = lines[line].split(";");
				for (int x = 0; x < dimX; x++) {
					String block = blocks[x];
					ComboBlock combo;
					String[] split = block.split(":");
					if (split.length == 2) combo = new ComboBlock(Integer.parseInt(split[0]), Byte.parseByte(split[1]));
					else combo = new ComboBlock(Integer.parseInt(split[0]));
					if (FALLOFF_MATERIALS.contains(Material.getMaterial(combo.getID())) == fall) setBlock(combo, x, y, z, mode, p);
				}
			}
		}
	}

	public void setBlock(ComboBlock block, int x, int y, int z, OverrideMode mode, Processor p) {
		Location offset = p.getOffset3D();
		if (offset == null) offset = p.getDispenser().getRelative(p.getTrain().getOppositeFace(), p.getOffset()).getLocation();
		Location start = new Location(offset.getWorld(), offset.getBlockX() + x, offset.getBlockY() + y, offset.getBlockZ() + z);
		Block change = start.getWorld().getBlockAt(start);
		if (block.getID() == 0 && mode != OverrideMode.ALL) return;
		if (block.getID() != 0 && change.getTypeId() != 0 && mode == OverrideMode.NONE) return;
		change.setTypeId(block.getID(), p.applyPhysics());
		change.setData(block.getData(), p.applyPhysics());
	}

	public void removeBlock(int x, int y, int z, Processor p, boolean fall) {
		Location offset = p.getOffset3D();
		if (offset == null) offset = p.getDispenser().getRelative(p.getTrain().getOppositeFace(), p.getOffset()).getLocation();
		Location start = new Location(offset.getWorld(), offset.getBlockX() + x, offset.getBlockY() + y, offset.getBlockZ() + z);
		Block change = start.getWorld().getBlockAt(start);
		if (FALLOFF_MATERIALS.contains(Material.getMaterial(change.getTypeId())) == fall) {
			change.setTypeId(0, p.applyPhysics());
			change.setData((byte) 0, p.applyPhysics());
		}
	}

	public boolean blockExists(ComboBlock block, int x, int y, int z, Processor p) {
		Location offset = p.getOffset3D();
		if (offset == null) offset = p.getDispenser().getRelative(p.getTrain().getOppositeFace(), p.getOffset()).getLocation();
		Location start = new Location(offset.getWorld(), offset.getBlockX() + x, offset.getBlockY() + y, offset.getBlockZ() + z);
		Block check = start.getWorld().getBlockAt(start);
		return check.getTypeId() == block.getID() && check.getData() == block.getData();
	}

}
