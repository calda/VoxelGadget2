package com.thevoxelbox.voxelgadget.modifier;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CheckGreaterModifier extends AbstractCheckModifier {

	@Override
	public boolean runCheck(Inventory target, Inventory dispenser, ItemStack block) {
		boolean match = true;
		for (ItemStack i : dispenser.getContents()) {
			if (i != null && i.getTypeId() != 0) {
				if (i.getTypeId() == block.getTypeId() && i.getData().getData() == block.getData().getData()) {
					i = new ItemStack(i.getType(), i.getAmount() + 1, i.getData().getData());
				}
				if (!inventoryHasProperStack(target, i)) match = false;
			}
		}
		if (!dispenser.contains(block.getTypeId())) {
			if (!inventoryHasProperStack(target, block)) match = false;
		}
		return match;
	}

	private boolean inventoryHasProperStack(Inventory other, ItemStack check) {
		for (ItemStack i : other.getContents()) {
			if (i != null && check.getTypeId() == i.getTypeId() && check.getData().getData() == i.getData().getData()) {
				System.out.println(i.getAmount() + " < " + check.getAmount());
				return i.getAmount() < check.getAmount();
			}
		}
		return false;
	}

}
