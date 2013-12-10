
package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GetInventoryModifier extends AbstractModeModifier {

	@Override
	public int modify(Processor p) {
		p.setMode(ModifierType.GET_INVENTORY);
		return 0;
	}
	
	@Override
	public int modeModify(Processor p){
		Block existing = p.getDispenser().getRelative(p.getTrain().getOppositeFace(), p.getOffset());
		if(p.getOffset3D() != null) existing = p.getOffset3D().getBlock();
		if(existing.getState() instanceof InventoryHolder){
			InventoryHolder block = (InventoryHolder) existing.getState();
			Inventory disp = ((Dispenser) p.getDispenser().getState()).getInventory();
			disp.setContents(block.getInventory().getContents());
		}
		return 0;
	}

}
