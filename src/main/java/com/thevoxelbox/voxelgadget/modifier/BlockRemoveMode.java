package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class BlockRemoveMode extends AbstractModeModifier {

    @Override
    public int modify(Processor p, Block nextBlock) {
		if (p.getDispensed().getTypeId() == 387) {
			(new BlueprintModifier()).remove(p);
			return 0;
		}
        Block existing = p.getDispenser().getRelative(p.getTail().getOppositeFace(), p.getOffset());
		if(p.getOffset3D() != null) existing = p.getOffset3D().getBlock();
        setBlock(existing, new ItemStack(0, p.getDispensed().getAmount(), (byte) 0), p.applyPhysics(), p);
        return 0;
    }

}
