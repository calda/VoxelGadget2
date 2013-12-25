package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * A special Modifier to easily create multiple OffsetModifiers which all do the same thing with different values.
 */
public class ThreeDimModifier extends AbstractModifier {

	@Override
	public int modify(Processor p, Block behind) {
		if (behind.getState() instanceof InventoryHolder) {
			Inventory iBehind = ((InventoryHolder) behind.getState()).getInventory();
			try {
				int offx = (iBehind.getItem(0) == null ? 0 : iBehind.getItem(0).getAmount() - 32);
				int offy = (iBehind.getItem(1) == null ? 0 : iBehind.getItem(1).getAmount() - 32);
				int offz = (iBehind.getItem(1) == null ? 0 : iBehind.getItem(2).getAmount() - 32);
				Location offsetTemp = new Location(p.getDispenser().getWorld(), offx, offy, offz);
				p.setOffset3D(p.getDispenser().getLocation().add(offsetTemp));
			} catch (NullPointerException e) {
				return 0;
			}
			return 1;
		}
		return 0;
	}

}
