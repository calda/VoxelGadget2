package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * A special Modifier to easily create multiple OffsetModifiers which all do the same thing with different values.
 */
public class Offset3DModifier extends AbstractModifier {
	
	@Override
	public int modify(Processor p, Block currentBlock, Block nextBlock) {
		if (nextBlock.getState() instanceof InventoryHolder) {
			Inventory iBehind = ((InventoryHolder) nextBlock.getState()).getInventory();
			Location offsetTarget = getOffsetFromInventory(iBehind, p);
			if (offsetTarget != null) p.setOffset3D(offsetTarget);
			return 1;
		}
		return 0;
	}
	
	public static Location getOffsetFromInventory(Inventory inv, Processor p) {
		try {
			int offx = (inv.getItem(0) == null ? 0 : inv.getItem(0).getAmount() - 32);
			int offy = (inv.getItem(1) == null ? 0 : inv.getItem(1).getAmount() - 32);
			int offz = (inv.getItem(1) == null ? 0 : inv.getItem(2).getAmount() - 32);
			Location offsetTemp = new Location(p.getDispenser().getWorld(), offx, offy, offz);
			return p.getDispenser().getLocation().add(offsetTemp);
		} catch (NullPointerException e) {
			return null;
		}
	}
}
