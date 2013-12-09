package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;

public class BlockRemoveMode extends AbstractModeModifier {

    @Override
    public int modify(Processor p) {
		if (p.getBlock().getTypeId() == 387) {
			(new BlueprintModifier()).remove(p);
			return 0;
		}
        Block existing = p.getDispenser().getRelative(p.getTrain().getOppositeFace(), p.getOffset());
		if(p.getOffset3D() != null) existing = p.getOffset3D().getBlock();
        setBlock(existing, 0, (byte) 0, p.applyPhysics(), p);
        return 0;
    }

}
