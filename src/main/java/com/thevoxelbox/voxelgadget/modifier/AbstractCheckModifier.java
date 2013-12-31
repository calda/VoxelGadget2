package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractCheckModifier extends AbstractModifier {

    @Override
    public int modify(Processor p, Block currentBlock, Block nextBlock) {
        Block existing = p.getTargetBlock();
        if (!(existing.getState() instanceof InventoryHolder)) {
            try {
                p.setCheck(runCheck(null, null, p.getDispensed(), existing, p.getInvOverride() != null) || p.getCheck());
                return 0;
            } catch (NullPointerException e) {
                return 0;
            }
        }
        Inventory target = ((InventoryHolder) existing.getState()).getInventory();
        Inventory dispenser;
        if (p.getInvOverride() != null) {
            dispenser = p.getInvOverride();
        } else {
            dispenser = ((Dispenser) p.getDispenser().getState()).getInventory();
        }

        p.setCheck(runCheck(target, dispenser, p.getDispensed(), existing, p.getInvOverride() != null) || p.getCheck());
        return 0;
    }

    public abstract boolean runCheck(Inventory target, Inventory dispenser, ItemStack dispensed, Block targetBlock, boolean invOverride);
}
