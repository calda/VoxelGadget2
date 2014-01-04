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
public class GetInventoryModifier extends AbstractModeModifier {

    @Override
    public int modify(Processor p, Block currentBlock, Block nextBlock) {
        p.setMode(ModifierType.GET_INVENTORY);
        if (nextBlock.getState() instanceof InventoryHolder) {
            p.setInvOverride(((InventoryHolder) nextBlock.getState()).getInventory());
            return 1;
        }
        return 0;
    }

    @Override
    public int modeModify(Processor p) {
        Block existing = p.getTargetBlock();
        if (existing.getState() instanceof InventoryHolder) {
            InventoryHolder target = (InventoryHolder) existing.getState();
            final Inventory disp = ((p.getInvOverride() == null) ? ((Dispenser) p.getDispenser().getState()).getInventory() : p.getInvOverride());
            final ItemStack[] newInventory = new ItemStack[disp.getSize()];
            if (p.getOverride() != null) {
                if (target.getInventory().contains(p.getOverride().getType())) {
                    newInventory[0] = target.getInventory().getItem(target.getInventory().first(p.getOverride().getType()));
                    disp.setContents(newInventory);
                }
            } else {
                for (int i = 0; i < Math.min(target.getInventory().getSize(), disp.getSize()); i++) {
                    newInventory[i] = target.getInventory().getItem(i);
                    if (newInventory[i] == null) {
                        newInventory[i] = new ItemStack(0, 1);
                    }
                    disp.setContents(newInventory);
                }
            }
        }
        return 0;
    }
}
