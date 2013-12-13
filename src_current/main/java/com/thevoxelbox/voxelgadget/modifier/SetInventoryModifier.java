package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class SetInventoryModifier extends AbstractModeModifier {

    @Override
    public int modify(Processor p) {
        p.setMode(ModifierType.SET_INVENTORY);
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
            Inventory disp = ((p.getInvOverride() == null) ? ((Dispenser) p.getDispenser().getState()).getInventory() : p.getInvOverride());
            block.getInventory().setContents(disp.getContents());
            block.getInventory().addItem(p.getBlock());
        }
        return 0;
    }
}
