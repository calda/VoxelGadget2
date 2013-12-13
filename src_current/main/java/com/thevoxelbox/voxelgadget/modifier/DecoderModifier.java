package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * A special Modifier to easily create multiple OffsetModifiers which all do the same thing with different values.
 */
public class DecoderModifier extends AbstractModifier {

	@Override
	public int modify(Processor p) {
		Block behind = p.getDispenser().getRelative(p.getTrain(), p.getCurrent() + 1);
		if (behind.getState() instanceof InventoryHolder) {
			Inventory iBehind = ((InventoryHolder) behind.getState()).getInventory();
			Inventory dispenser = ((Dispenser) p.getDispenser().getState()).getInventory();
			ItemStack firstFound = null;
			for (ItemStack item : dispenser.getContents()) {
				if (item != null && item.getTypeId() == p.getBlock().getTypeId() && item.getData().getData() == p.getBlock().getData().getData()) {
					firstFound = item;
					break;
				}
			}
			if (firstFound != null) {
				int amount = firstFound.getAmount();
				if(amount > iBehind.getSize()) amount = iBehind.getSize() - 1;
				ItemStack replace = iBehind.getItem(amount);
				if(replace == null) replace = new ItemStack(0);
				p.setBlock(replace);
			}
			return 1;
		}
		return 0;

	}

}
