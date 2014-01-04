package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * @author CalDaBeast
 */
public class LineModifier extends AbstractModifier {

	@Override
	public int modify(Processor p, Block currentBlock, Block nextBlock) {
		p.setAreaEnabled(false);
		p.setLineEnabled(true);
		return 0;
	}

	protected static void create(Processor p, AbstractModeModifier mode, ItemStack dispensed){
		int length = p.getOffset() - 1;
		int offset = p.getSize();
		for (int i = 0; i < length; i++) {
			Block set = p.getDispenser().getRelative(p.getTail().getOppositeFace(), i + offset + 2);
			if(p.getOffset3D() != null) set = p.getOffset3D().getBlock().getRelative(p.getTail().getOppositeFace(), i);
			mode.actualSetBlock(set, dispensed, p.applyPhysics(), p);
		}
	}
	
}
