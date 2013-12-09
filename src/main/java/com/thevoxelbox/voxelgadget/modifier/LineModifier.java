package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;

public class LineModifier extends AbstractModifier {

	@Override
	public int modify(Processor p) {
		p.setAreaEnabled(false);
		p.setLineEnabled(true);
		return 0;
	}

}
