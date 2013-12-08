package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BlockToggleMode extends AbstractModeModifier {

    @Override
    public boolean modify(Processor p) {
        Block existing = p.dispenser.getRelative(p.train.getOppositeFace(), (p.lineEnabled || p.areaEnabled ? p.getSize() + (p.lineEnabled ? 2 : 1) : p.getOffset()));
        if (p.overrideAbsolute) {
            if (existing.getTypeId() == p.override.getTypeId() && existing.getData() == p.override.getData()) {
                setBlock(existing, 0, (byte) 0, p.applyPhysics, p);
            } else setBlock(existing, p.override.getTypeId(), (byte) p.override.getData(), p.applyPhysics, p);
            return true;
        }
        Block dispenser = p.dispenser;
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
            if (p.override == null) setBlock(existing, 0, (byte) 0, p.applyPhysics, p);
            else setBlock(existing, p.override.getTypeId(), (byte) p.override.getData(), p.applyPhysics, p);
        } else setBlock(existing, p.block.getTypeId(), (byte) p.block.getData().getData(), p.applyPhysics, p);
        return true;
    }
}
