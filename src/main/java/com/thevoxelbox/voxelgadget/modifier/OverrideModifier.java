package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class OverrideModifier extends AbstractModifier {

	@Override
	public int modify(Processor p, Block currentBlock, Block behind1) {
		Block behind2 = currentBlock.getRelative(currentBlock.getFace(behind1), 2);
		Block behind3 = currentBlock.getRelative(currentBlock.getFace(behind1), 3);
		if (p.getModifierFromConfig(new ComboBlock(behind3)) == ModifierType.OVERRIDE) {
			p.setDispensed(new ItemStack(behind2.getTypeId(), p.getDispensed().getAmount(), behind2.getData()));
			p.setOverride(behind1);
			p.setOverrideAbsolute(true);
			return 3;
		} else if (p.getModifierFromConfig(new ComboBlock(behind2)) == ModifierType.OVERRIDE) {
			p.setOverride(behind1);
			p.setOverrideAbsolute(true);
			return 2;
		} else {
			p.setOverride(behind1);
			return 1;
		}
	}

}
