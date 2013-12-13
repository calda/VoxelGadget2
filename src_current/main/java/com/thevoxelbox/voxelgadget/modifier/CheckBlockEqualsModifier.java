package com.thevoxelbox.voxelgadget.modifier;

import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CheckBlockEqualsModifier extends AbstractCheckModifier {

	@Override
	public boolean runCheck(Inventory target, Inventory dispenser, ItemStack dispensed, Block targetBlock, boolean invOverride) {
		return targetBlock != null && targetBlock.getTypeId() == dispensed.getTypeId() && targetBlock.getData() == dispensed.getData().getData();
	}

}
