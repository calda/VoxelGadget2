package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

public class AreaModifier extends AbstractModifier {

	@Override
	public int modify(Processor p, Block currentBlock, Block nextBlock) {
		p.setAreaEnabled(true);
		p.setLineEnabled(false);
		return 0;
	}

	protected static void create(Processor p, AbstractModeModifier mode, ItemStack dispensed){
		int radius = p.getOffset() - 1;
		int offset = p.getSize();
		if (radius > 5) radius = 5;
		if (offset == -1) offset = 0;
		Block center = p.getDispenser().getRelative(p.getTail().getOppositeFace(), offset + 1);
		if(p.getOffset3D() != null) center = p.getOffset3D().getBlock();
		for (int i = 0 - radius; i <= radius; i++) {
			for (int j = 0 - radius; j <= radius; j++) {
				Block set = null;
				if (p.getTail() == BlockFace.EAST || p.getTail() == BlockFace.WEST) {
					set = center.getRelative(BlockFace.UP, i).getRelative(BlockFace.SOUTH, j);
				} else if (p.getTail() == BlockFace.NORTH || p.getTail() == BlockFace.SOUTH) {
					set = center.getRelative(BlockFace.UP, i).getRelative(BlockFace.EAST, j);
				} else if (p.getTail() == BlockFace.UP || p.getTail() == BlockFace.DOWN) {
					set = center.getRelative(BlockFace.SOUTH, i).getRelative(BlockFace.EAST, j);
				}
				if (set != null) {
					mode.actualSetBlock(set, dispensed, p.applyPhysics(), p);
				}
			}
		}
	}
	
}
