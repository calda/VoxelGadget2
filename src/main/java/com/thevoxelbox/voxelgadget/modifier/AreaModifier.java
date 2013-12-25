package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;

public class AreaModifier extends AbstractModifier {

	@Override
	public int modify(Processor p, Block nextBlock) {
		p.setAreaEnabled(true);
		p.setLineEnabled(false);
		return 0;
	}

}
