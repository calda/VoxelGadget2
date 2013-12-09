package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;

public class BlockPlaceMode extends AbstractModeModifier {

	@Override
	public boolean modify(Processor p) {
		if (p.getBlock().getTypeId() == 387) {
			(new BlueprintModifier()).paste(p);
			return true;
		}
		Block existing = p.getDispenser().getRelative(p.getTrain().getOppositeFace(), p.getOffset());
		if (p.getOffset3D() != null) existing = p.getOffset3D().getBlock();
		if (p.getOverride() == null) setBlock(existing, p.getBlock().getTypeId(), (byte) p.getBlock().getData().getData(), p.applyPhysics(), p);
		else setBlock(existing, p.getOverride().getTypeId(), (byte) p.getOverride().getData(), p.applyPhysics(), p);
		return true;
	}

}
