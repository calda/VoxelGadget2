
package com.thevoxelbox.gadget.modifier;

import com.thevoxelbox.gadget.Processor;
import org.bukkit.block.Block;

public class BlockPlaceModifier extends AbstractModifier{

    @Override
    public boolean modify(Processor p) {
	Block existing = p.dispenser.getRelative(p.train.getOppositeFace(), p.offset + 1);
	if(p.override == null) setBlock(existing, p.block.getTypeId(), (byte)p.block.getData().getData(), p.applyPhysics);
	else setBlock(existing, p.override.getTypeId(), (byte)p.override.getData(), p.applyPhysics);
	return true;
    }

}
