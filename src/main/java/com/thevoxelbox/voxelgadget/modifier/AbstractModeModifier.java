package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractModeModifier extends AbstractModifier {

	/**
	 * Does necessary logic to prepare for a Mode Modifier to do it's own logic.
	 *
	 * @param p the Processor that called the method
	 * @return true if successful
	 */
	public int modeModify(Processor p) {
		Block existing = p.getDispenser().getRelative(p.getTrain().getOppositeFace(), p.getOffset());
		if (p.getOffset3D() != null) existing = p.getDispenser().getLocation().add(p.getOffset3D()).getBlock();
		if (p.getFilter() == null) return modify(p);
		else if (existing.getTypeId() == p.getFilter().getTypeId() && existing.getData() == p.getFilter().getData()) return modify(p);
		else return 0;
	}

	/**
	 * The method used by Mode Modifiers to place and remove blocks.
	 * Places the block in a chest if there is a chest present.
	 * Also checks if it can be placed under the Finite modifier.
	 *
	 * @param existing the block already in the world to be overriden
	 * @param newID the ID of the block to be placed
	 * @param newData the data/ink of the block to be placed
	 * @param applyPhysics if Physics should be applied on the placing/removing
	 * @param p the Processor that called the Mode Modifier to be triggered
	 */
	protected void setBlock(Block existing, int newID, byte newData, boolean applyPhysics, Processor p) {
		Dispenser d = (Dispenser) p.getDispenser().getState();
		if (newID == 46 && (p.getOverride() == null || p.getOverride().getTypeId() != 46)) { //tnt; not overriden
			existing.getWorld().spawnEntity(existing.getLocation(), EntityType.PRIMED_TNT);
			return;
		}
		if (p.isAreaEnabled()) {
			int radius = p.getOffset() - 1;
			int offset = p.getSize();
			if (radius > 5) radius = 5;
			if (offset == -1) offset = 0;
			Block center = p.getDispenser().getRelative(p.getTrain().getOppositeFace(), offset + 1);
			for (int i = 0 - radius; i <= radius; i++) {
				for (int j = 0 - radius; j <= radius; j++) {
					Block set = null;
					if (p.getTrain() == BlockFace.EAST || p.getTrain() == BlockFace.WEST) {
						set = center.getRelative(BlockFace.UP, i).getRelative(BlockFace.SOUTH, j);
					} else if (p.getTrain() == BlockFace.NORTH || p.getTrain() == BlockFace.SOUTH) {
						set = center.getRelative(BlockFace.UP, i).getRelative(BlockFace.EAST, j);
					} else if (p.getTrain() == BlockFace.UP || p.getTrain() == BlockFace.DOWN) {
						set = center.getRelative(BlockFace.SOUTH, i).getRelative(BlockFace.EAST, j);
					}
					if (set != null) {
						actualSetBlock(d, set, newID, newData, applyPhysics, p);
					}
				}
			}
		} else if (p.isLineEnabled()) {
			int length = p.getOffset() - 1;
			int offset = p.getSize();
			//System.out.println("l:" + length + " o:" + offset);
			for (int i = 0; i < length; i++) {
				Block set = p.getDispenser().getRelative(p.getTrain().getOppositeFace(), i + offset + 2);
				actualSetBlock(d, set, newID, newData, applyPhysics, p);
			}
		} else actualSetBlock(d, existing, newID, newData, applyPhysics, p);
	}

	private void actualSetBlock(Dispenser d, Block existing, int newID, byte newData, boolean applyPhysics, Processor p) {
		if (existing.getState() instanceof InventoryHolder) {
			InventoryHolder invBlock = (InventoryHolder) existing.getState();
			Inventory i = invBlock.getInventory();
			//if (i.firstEmpty() != -1) i.addItem(new ItemStack(newID, 1, newData));
			int amount = 0;
			for (ItemStack is : d.getInventory().getContents()) {
				if (is != null && is.getTypeId() == p.getBlock().getTypeId() && is.getData().getData() == p.getBlock().getData().getData()) {
					amount += is.getAmount() + 1;
				}
			}
			if (newID == 0) {
				i.removeItem(new ItemStack(p.getBlock().getTypeId(), amount, p.getBlock().getData().getData()));
			} else {
				i.addItem(new ItemStack(newID, amount, newData));
			}
		} else {
			existing.setTypeId(newID, applyPhysics);
			existing.setData(newData, applyPhysics);
		}
	}

}
