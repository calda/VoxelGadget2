
package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;

public class TimerModifier extends AbstractModifier{

	@Override
	public int modify(Processor p) {
		p.setTimerEnabled(true);
		return 0;
	}

}
