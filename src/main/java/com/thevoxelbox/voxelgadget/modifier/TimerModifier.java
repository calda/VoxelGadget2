
package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;

public class TimerModifier extends AbstractModifier {
	
	@Override
	public int modify(Processor p, Block currentBlock, Block nextBlock) {
		if (p.isTimerEnabled()) {
			p.setTriggerExtendsTimer(true);
		} else {
			p.setTimerEnabled(true);
		}
		return 0;
	}
	
}
