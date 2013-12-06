
package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;

public class BlockPlaceMode extends AbstractModeModifier{

    @Override
    public boolean modify(Processor p) {
	Block existing = p.dispenser.getRelative(p.train.getOppositeFace(), p.getOffset());
	if(p.override == null) setBlock(existing, p.block.getTypeId(), (byte)p.block.getData().getData(), p.applyPhysics, p);
	else setBlock(existing, p.override.getTypeId(), (byte)p.override.getData(), p.applyPhysics, p);
	return true;
    }

}
