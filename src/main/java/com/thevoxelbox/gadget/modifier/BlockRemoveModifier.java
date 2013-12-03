
package com.thevoxelbox.gadget.modifier;

import com.thevoxelbox.gadget.Processor;
import org.bukkit.block.Block;

public class BlockRemoveModifier extends AbstractModifier{

    @Override
    public boolean modify(Processor p) {
	Block existing = p.dispenser.getRelative(p.train.getOppositeFace(), p.offset + 1);
	setBlock(existing, 0,(byte)0, p.applyPhysics);
	return true;
    }

}