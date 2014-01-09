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
public abstract class AbstractCheckModifier extends AbstractModifier {

	@Override
	public int modify(Processor p, Block currentBlock, Block nextBlock) {
		Block existing = p.getTargetBlock();
		Inventory invOverride = null;
		if (nextBlock.getState() instanceof InventoryHolder) {
			invOverride = ((InventoryHolder) nextBlock.getState()).getInventory();
		}
		if (!(existing.getState() instanceof InventoryHolder)) {
			try {
				p.setCheck(runCheck(null, (invOverride == null ? ((Dispenser) p.getDispenser().getState()).getInventory()
						: invOverride), p.getDispensed(), existing, invOverride != null) || p.getCheck());
				return 0;
			} catch (NullPointerException e) {
				return 0;
			}
		}
		Inventory target = ((InventoryHolder) existing.getState()).getInventory();
		Inventory dispenser;
		if (invOverride != null) {
			dispenser = invOverride;
		} else {
			dispenser = ((Dispenser) p.getDispenser().getState()).getInventory();
		}

		p.setCheck(runCheck(target, dispenser, p.getDispensed(), existing, invOverride != null) || p.getCheck());
		return 0;
	}

	public abstract boolean runCheck(Inventory target, Inventory dispenser, ItemStack dispensed, Block targetBlock, boolean invOverride);
}
