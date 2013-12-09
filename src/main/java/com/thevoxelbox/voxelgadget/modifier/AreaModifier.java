package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;

public class AreaModifier extends AbstractModifier {

	@Override
	public int modify(Processor p) {
		p.setAreaEnabled(true);
		p.setLineEnabled(false);
		return 0;
	}

}
