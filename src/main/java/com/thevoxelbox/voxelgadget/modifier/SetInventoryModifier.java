package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * @author CalDaBeast
 */
public class SetInventoryModifier extends AbstractModeModifier {

	private Inventory invOverride = null;
	
    @Override
    public int modify(Processor p, Block currentBlock, Block nextBlock) {
        p.setMode(ModifierType.SET_INVENTORY);
        if (nextBlock.getState() instanceof InventoryHolder) {
            invOverride = ((InventoryHolder) nextBlock.getState()).getInventory();
            return 1;
        }
		invOverride = null;
        return 0;
    }

    @Override
    public int modeModify(Processor p) {
        Block existing =p.getTargetBlock();
        if (existing.getState() instanceof InventoryHolder) {
            Inventory target = ((InventoryHolder) existing.getState()).getInventory();
            Inventory disp = ((invOverride == null) ? ((Dispenser) p.getDispenser().getState()).getInventory() : invOverride);
            if (invOverride != null) {
                if (p.getOverride() != null) {
                    if (disp.contains(p.getOverride().getType()) && target.contains(p.getOverride().getType())) {
                        target.setItem(target.first(p.getOverride().getType()), disp.getItem(disp.first(p.getOverride().getType())));
                    }
                } else {
                    target.setContents(disp.getContents());
                }
            } else {
                if (p.getOverride() != null) {
                    if (disp.contains(p.getOverride().getType()) && target.contains(p.getOverride().getType())) {
                        target.setItem(target.first(p.getOverride().getType()), disp.getItem(disp.first(p.getOverride().getType())));
                        if (p.getOverride().getType().equals(p.getDispensed().getType())) {
                            target.addItem(p.getDispensed());
                        }
                    }
                } else {
                    target.setContents(disp.getContents());
                    target.addItem(new ItemStack(p.getDispensed().getTypeId(), 1, p.getDispensed().getData().getData()));
                }
            }
        }
        return 0;
    }
}
