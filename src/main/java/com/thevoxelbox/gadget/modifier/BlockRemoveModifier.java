
package com.thevoxelbox.gadget.modifier;

import com.thevoxelbox.gadget.Processor;
import org.bukkit.block.Block;

public class BlockRemoveModifier extends AbstractModifier{

    @Override
    public boolean modify(Processor p) {
	Block change = p.dispenser.getRelative(p.train.getOppositeFace(), p.offset + 1);
	change.setTypeId(0, p.applyPhysics);
	change.setData((byte)0, p.applyPhysics);
	return true;
    }

}
