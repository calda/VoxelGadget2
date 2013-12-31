package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;

public class SkipModifier extends AbstractModifier {

	@Override
	public int modify(Processor p, Block currentBlock, Block nextBlock) {
		if (p.isTimerEnabled()) {
			p.setWillSkipFirst(true);
			return 0;
		} else {
			return 1;
		}
	}

}
