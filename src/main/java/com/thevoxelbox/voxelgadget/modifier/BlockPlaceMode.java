package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * @author CalDaBeast
 */
public class BlockPlaceMode extends AbstractModeModifier {

	@Override
	public int modify(Processor p, Block currentBlock, Block nextBlock) {
		if (p.getDispensed().getTypeId() == 387) {
			try {
				BlueprintHandler blueprint = new BlueprintHandler(p.getDispensed());
				blueprint.paste(p.getTargetLocation());
			} catch (Exception e) {
				BlueprintHandler.handleException(e, p.getDispensed());
				System.out.println(p.getDispensed().getItemMeta());
			}
			return 0;
		}
		Block existing = p.getTargetBlock();
		if (p.getOverride() == null) setBlock(existing, p.getDispensed(), p.applyPhysics(), p);
		else setBlock(existing, new ItemStack(p.getOverride().getTypeId(), p.getDispensed().getAmount(), p.getOverride().getData()), p.applyPhysics(), p);
		return 0;
	}

}
