package com.thevoxelbox.voxelgadget.modifier;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CheckEqualsModifier extends AbstractCheckModifier {

	@Override
	public boolean runCheck(Inventory target, Inventory dispenser, ItemStack block) {
		boolean match = true;
		for (ItemStack i : dispenser.getContents()) {
			if (i != null && i.getTypeId() != 0) {
				if (i.getTypeId() == block.getTypeId() && i.getData().getData() == block.getData().getData()) {
					i = new ItemStack(i.getType(), i.getAmount() + 1, i.getData().getData());
				}
				if (!target.contains(i)) match = false;
			}
		}if(!dispenser.contains(block.getTypeId())){
			if (!target.contains(block)) match = false;
		}
		return match;
	}

}
