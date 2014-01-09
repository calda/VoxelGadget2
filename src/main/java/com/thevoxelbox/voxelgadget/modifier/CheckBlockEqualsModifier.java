package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author CalDaBeast
 */
public class CheckBlockEqualsModifier extends AbstractCheckModifier {

	@Override
	public boolean runCheck(Inventory target, Inventory dispenser, ItemStack dispensed, Block targetBlock, boolean invOverride) {
		if (invOverride) {
			ItemStack random = Processor.getRandomBlockFromInventory(dispenser);
			if (random != null) dispensed = random;
		}
		return targetBlock != null && targetBlock.getTypeId() == dispensed.getTypeId() && targetBlock.getData() == dispensed.getData().getData();
	}

}
