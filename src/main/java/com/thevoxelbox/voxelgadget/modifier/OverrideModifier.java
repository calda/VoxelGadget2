package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;

public class OverrideModifier extends AbstractModifier {

	@Override
	public int modify(Processor p, Block nextBlock) {
		if (p.getOverride() != null) {
			p.setOverrideAbsolute(true);
			return 0;
		} else {
			p.setOverride(nextBlock);
			return 1;
		}
	}

}
