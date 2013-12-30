package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
		Block existing = p.getTargetBlock();
		if (p.getFilter() == null) return modify(p, null);
		else if (existing.getTypeId() == p.getFilter().getTypeId() && existing.getData() == p.getFilter().getData()) return modify(p, null);
		else return 0;
	}

	/**
	 * The method used by Mode Modifiers to place and remove blocks.
	 * Places the block in a chest if there is a chest present.
	 * Also checks if it can be placed under the Finite modifier.
	 *
	 * @param existing the block already in the world to be overridden
	 * @param dispensed the block dispensed by the dispenser
	 * @param applyPhysics if Physics should be applied on the placing/removing
	 * @param p the Processor that called the Mode Modifier to be triggered
	 */
	protected void setBlock(Block existing, ItemStack dispensed, boolean applyPhysics, Processor p) {
		if (dispensed.getType() == Material.TNT && (p.getOverride() == null || p.getOverride().getType() == Material.TNT)) { //tnt; not overriden
			existing.getWorld().spawnEntity(existing.getLocation(), EntityType.PRIMED_TNT);
			return;
		}
		if (p.isAreaEnabled()) AreaModifier.create(p, this, dispensed);
		else if (p.isLineEnabled()) LineModifier.create(p, this, dispensed);
		else actualSetBlock(existing, dispensed, applyPhysics, p);
	}

	protected void actualSetBlock(Block existing, ItemStack dispensed, boolean applyPhysics, Processor p) {
		int newID = dispensed.getTypeId();
		byte newData = dispensed.getData().getData();
		if (existing.getState() instanceof InventoryHolder) {
			Inventory inv = ((InventoryHolder) existing.getState()).getInventory();
			if (newID == 0) {
				inv.removeItem(new ItemStack(p.getDispensed().getTypeId(), dispensed.getAmount(), p.getDispensed().getData().getData()));
			} else {
				inv.addItem(new ItemStack(newID, dispensed.getAmount(), newData));
			}
		} else {
			existing.setTypeId(newID, applyPhysics);
			existing.setData(newData, applyPhysics);
		}
	}

}
