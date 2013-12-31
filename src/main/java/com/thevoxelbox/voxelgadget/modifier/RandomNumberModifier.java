package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import java.util.Random;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class RandomNumberModifier extends AbstractModifier {

	@Override
	public int modify(Processor p, Block currentBlock, Block nextBlock) {
		Random r = new Random();
		if (nextBlock.getState() instanceof InventoryHolder) {
			Inventory inv = ((InventoryHolder) nextBlock.getState()).getInventory();
			int min = inv.getItem(0) == null ? 0 : inv.getItem(0).getAmount();
			int max = inv.getItem(1) == null ? 64 : inv.getItem(1).getAmount();
			if (min > max || max < min) return 1;
			p.getDispensed().setAmount(r.nextInt(max - min + 1) + min);
			return 1;
		}
		p.getDispensed().setAmount(r.nextInt(64) + 1);
		return 0;
	}

}
