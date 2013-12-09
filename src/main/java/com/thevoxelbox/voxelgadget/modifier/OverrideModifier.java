package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;

public class OverrideModifier extends AbstractModifier {

	@Override
	public int modify(Processor p) {
		if (p.getOverride() != null) {
			ModifierType twoAgo = p.getModifierFromConfig(new ComboBlock(p.getDispenser().getRelative(p.getTrain(), p.getCurrent() + 2)));
			if (twoAgo == ModifierType.OVERRIDE) p.setOverrideAbsolute(true);
			return 0;
		} else {
			p.setOverride(p.getDispenser().getRelative(p.getTrain(), p.getCurrent() + 1));
			return 1;
		}
	}

}
