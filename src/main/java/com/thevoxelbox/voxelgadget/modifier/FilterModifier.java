package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;

public class FilterModifier extends AbstractModifier {

	@Override
	public int modify(Processor p) {
		p.setFilter(p.getDispenser().getRelative(p.getTrain(), p.getCurrent() + 1));
		return 1;
	}

}
