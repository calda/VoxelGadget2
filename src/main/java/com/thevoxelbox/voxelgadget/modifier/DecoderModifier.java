package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * @author CalDaBeast
 */
public class DecoderModifier extends AbstractModifier {

	@Override
	public int modify(Processor p, Block currentBlock, Block nextBlock) {
		if (nextBlock.getState() instanceof InventoryHolder) {
			Inventory iBehind = ((InventoryHolder) nextBlock.getState()).getInventory();
			Inventory dispenser = ((Dispenser) p.getDispenser().getState()).getInventory();
			ItemStack firstFound = null;
			for (ItemStack item : dispenser.getContents()) {
				if (item != null && item.getTypeId() == p.getDispensed().getTypeId() && item.getData().getData() == p.getDispensed().getData().getData()) {
					firstFound = item;
					break;
				}
			}
			if (firstFound != null) {
				int amount = firstFound.getAmount();
				if(amount > iBehind.getSize()) amount = iBehind.getSize() - 1;
				ItemStack replace = iBehind.getItem(amount);
				if(replace == null) replace = new ItemStack(0);
				p.setDispensed(replace);
			}
			return 1;
		}
		return 0;

	}

}
