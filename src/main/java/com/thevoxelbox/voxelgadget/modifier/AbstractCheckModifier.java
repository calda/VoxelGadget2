package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractCheckModifier extends AbstractModifier {

	@Override
	public int modify(Processor p) {
		Block existing = p.getDispenser().getRelative(p.getTrain().getOppositeFace(), p.getOffset());
		if (p.getOffset3D() != null) existing = p.getOffset3D().getBlock();
		if (!(existing.getState() instanceof InventoryHolder)) return 0;
		Inventory target = ((InventoryHolder) existing.getState()).getInventory();
		Inventory dispenser = ((Dispenser) p.getDispenser().getState()).getInventory();
		p.setCheck(runCheck(target, dispenser, p.getBlock()) || p.getCheck());
		return 0;
	}

	public abstract boolean runCheck(Inventory target, Inventory dispenser, ItemStack dispensed);

}
