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
		int offset = 0;
		Block existing = p.getDispenser().getRelative(p.getTrain().getOppositeFace(), p.getOffset());
		if (p.getOffset3D() != null) {
			existing = p.getOffset3D().getBlock();
		}
		if (!(existing.getState() instanceof InventoryHolder)) {
			try {
				p.setCheck(runCheck(null, null, p.getBlock(), existing) || p.getCheck());
				return 0;
			} catch (NullPointerException e) {
			}
		}
		Inventory target = ((InventoryHolder) existing.getState()).getInventory();
		Block behind = p.getDispenser().getRelative(p.getTrain(), p.getCurrent() + 1);
		Inventory dispenser;
		if (behind.getState() instanceof InventoryHolder) {
			dispenser = ((InventoryHolder) behind.getState()).getInventory();
			offset = 1;
		} else {
			dispenser = ((Dispenser) p.getDispenser().getState()).getInventory();
		}
		p.setCheck(runCheck(target, dispenser, p.getBlock(), existing) || p.getCheck());
		return offset;
	}

	public abstract boolean runCheck(Inventory target, Inventory dispenser, ItemStack dispensed, Block targetBlock);
	
}
