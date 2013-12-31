package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceMode extends AbstractModeModifier {

	@Override
	public int modify(Processor p, Block currentBlock, Block nextBlock) {
		if (p.getDispensed().getTypeId() == 387) {
			(new BlueprintModifier()).paste(p);
			return 0;
		}
		Block existing = p.getTargetBlock();
		if (p.getOverride() == null) setBlock(existing, p.getDispensed(), p.applyPhysics(), p);
		else setBlock(existing, new ItemStack(p.getOverride().getTypeId(), p.getDispensed().getAmount(), p.getOverride().getData() ), p.applyPhysics(), p);
		return 0;
	}

}
