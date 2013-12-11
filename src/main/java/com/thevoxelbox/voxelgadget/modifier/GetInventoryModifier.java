package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class GetInventoryModifier extends AbstractModeModifier {

    @Override
    public int modify(Processor p) {
        p.setMode(ModifierType.GET_INVENTORY);
        Block behind = p.getDispenser().getRelative(p.getTrain(), p.getCurrent() + 1);
        if (behind.getState() instanceof InventoryHolder) {
            p.setInvOverride(((InventoryHolder) behind.getState()).getInventory());
            return 1;
        }
        return 0;
    }

    @Override
    public int modeModify(Processor p) {
        Block existing = p.getDispenser().getRelative(p.getTrain().getOppositeFace(), p.getOffset());
        if (p.getOffset3D() != null) {
            existing = p.getOffset3D().getBlock();
        }
        if (existing.getState() instanceof InventoryHolder) {
            InventoryHolder block = (InventoryHolder) existing.getState();
            final Inventory disp = ((p.getInvOverride() == null) ? ((Dispenser) p.getDispenser().getState()).getInventory() : p.getInvOverride());
            final ItemStack[] newInventory = new ItemStack[disp.getSize()];
            for (int i = 0; i < Math.min(block.getInventory().getSize(), disp.getSize()); i++) {
                newInventory[i] = block.getInventory().getItem(i);
                if (newInventory[i] == null) {
                    newInventory[i] = new ItemStack(0, 1);
                }
                disp.setContents(newInventory);
            }
        }
        return 0;
    }
}
