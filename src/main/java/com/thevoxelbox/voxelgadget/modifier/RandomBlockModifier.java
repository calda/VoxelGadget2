package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class RandomBlockModifier extends AbstractModifier {

	@Override
	public int modify(Processor p, Block currentBlock, Block nextBlock) {
		if (nextBlock.getState() instanceof InventoryHolder) {
			Inventory inv = ((InventoryHolder) nextBlock.getState()).getInventory();
			HashMap<ComboBlock, int[]> ranges = new HashMap<>();
			int currentHigh = 0;
			for (ItemStack stack : inv) {
				if (stack != null) {
					int[] range = new int[2];
					range[0] = currentHigh + 1;
					range[1] = range[0] + stack.getAmount();
					currentHigh = range[1];
					ranges.put(new ComboBlock(stack.getTypeId(), stack.getData().getData()), range);
				}
			}
			if (ranges.isEmpty()) return 0;
			Random r = new Random();
			int randomIndex = r.nextInt(currentHigh + 1);
			ComboBlock selected = null;
			for (Entry<ComboBlock, int[]> rangeEntry : ranges.entrySet()) {
				if (rangeEntry.getValue()[0] <= randomIndex && randomIndex <= rangeEntry.getValue()[1]) {
					selected = rangeEntry.getKey();
					break;
				}
			}
			if (selected != null) {
				p.setDispensed(new ItemStack(selected.getID(), p.getDispensed().getAmount(), selected.getData()));
				return 1;
			} else return 0;

		}
		return 0;
	}
}
