
package com.thevoxelbox.gadget.modifier;

import com.thevoxelbox.gadget.Processor;
import org.bukkit.block.Block;

public class BlockRemoveMode extends AbstractModeModifier{

    @Override
    public boolean modify(Processor p) {
	Block existing = p.dispenser.getRelative(p.train.getOppositeFace(), p.getOffset());
	setBlock(existing, 0,(byte)0, p.applyPhysics, p);
	return true;
    }

}