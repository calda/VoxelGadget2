package com.thevoxelbox.voxelgadget.modifier;

import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CheckEqualsModifier extends AbstractCheckModifier {

	@Override
	public boolean runCheck(Inventory target, Inventory dispenser, ItemStack dispensed, Block targetBlock) {
		boolean match = true;
		for (ItemStack i : dispenser.getContents()) {
			if (i != null && i.getTypeId() != 0) {
				if (i.getTypeId() == dispensed.getTypeId() && i.getData().getData() == dispensed.getData().getData()) {
					i = new ItemStack(i.getType(), i.getAmount() + 1, i.getData().getData());
				}
				if (!target.contains(i)) match = false;
			}
		}if(!dispenser.contains(dispensed.getTypeId())){
			if (!target.contains(dispensed)) match = false;
		}
		return match;
	}

}
