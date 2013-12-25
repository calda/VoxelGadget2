package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;

public class LineModifier extends AbstractModifier {

	@Override
	public int modify(Processor p, Block nextBlock) {
		p.setAreaEnabled(false);
		p.setLineEnabled(true);
		return 0;
	}

}
