package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;

public class FilterModifier extends AbstractModifier {

	@Override
	public int modify(Processor p, Block currentBlock, Block nextBlock) {
		p.setFilter(p.getDispenser().getRelative(p.getTail(), p.getCurrent() + 1));
		return 1;
	}

}
