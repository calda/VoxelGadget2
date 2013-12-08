package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BlockToggleMode extends AbstractModeModifier {

    @Override
    public boolean modify(Processor p) {
        Block existing = p.getDispenser().getRelative(p.getTrain().getOppositeFace(), (p.isLineEnabled() || p.isAreaEnabled() ? p.getSize() + (p.isLineEnabled() ? 2 : 1) : p.getOffset()));
        if (p.isOverrideAbsolute()) {
            if (existing.getTypeId() == p.getOverride().getTypeId() && existing.getData() == p.getOverride().getData()) {
                setBlock(existing, 0, (byte) 0, p.applyPhysics(), p);
            } else setBlock(existing, p.getOverride().getTypeId(), (byte) p.getOverride().getData(), p.applyPhysics(), p);
            return true;
        }
        Block dispenser = p.getDispenser();
        Dispenser disp = (Dispenser) dispenser.getState();
        Inventory inv = disp.getInventory();
        boolean existingInDispenser = false;
        for (ItemStack item : inv.getContents()) {
            if (item != null && item.getTypeId() == existing.getTypeId() && item.getData().getData() == existing.getData()) {
                existingInDispenser = true;
                break;
            }
        }
        if (existingInDispenser) { //dispensed block
            if (p.getOverride() == null) setBlock(existing, 0, (byte) 0, p.applyPhysics(), p);
            else setBlock(existing, p.getOverride().getTypeId(), (byte) p.getOverride().getData(), p.applyPhysics(), p);
        } else setBlock(existing, p.getBlock().getTypeId(), (byte) p.getBlock().getData().getData(), p.applyPhysics(), p);
        return true;
    }
}
