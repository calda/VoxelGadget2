package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;

import java.util.HashMap;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractModeModifier extends AbstractModifier {
    
    /**
     * Does necessary logic to prepare for a Mode Modifier to do it's own logic.
     * @param p the Processor that called the method
     * @return true if successful
     */
    public boolean modeModify(Processor p) {
        Block existing = p.getDispenser().getRelative(p.getTrain().getOppositeFace(), p.getOffset());
        if (p.getFilter() == null) return modify(p);
        else if (existing.getTypeId() == p.getFilter().getTypeId() && existing.getData() == p.getFilter().getData()) return modify(p);
        else return true;
    }

    /**
     * The method used by Mode Modifiers to place and remove blocks.
     * Places the block in a chest if there is a chest present.
     * Also checks if it can be placed under the Finite modifier.
     * 
     * @param existing the block already in the world to be overriden
     * @param newID the ID of the block to be placed
     * @param newData the data/ink of the block to be placed
     * @param applyPhysics if Physics should be applied on the placing/removing
     * @param p the Processor that called the Mode Modifier to be triggered
     */
    protected void setBlock(Block existing, int newID, byte newData, boolean applyPhysics, Processor p) {
        Dispenser d = (Dispenser) p.getDispenser().getState();
        if (newID == 46 && (p.getOverride() == null || p.getOverride().getTypeId() != 46)) { //tnt; not overriden
			existing.getWorld().spawnEntity(existing.getLocation(), EntityType.PRIMED_TNT);
            return;
        }
        if (existing.getState() instanceof InventoryHolder) {
            if (newID == 0) return;
            InventoryHolder invBlock = (InventoryHolder) existing.getState();
            Inventory i = invBlock.getInventory();
            if (i.firstEmpty() != -1) i.addItem(new ItemStack(newID, 1, newData));
        } else {
            if (p.isAreaEnabled()) {
                int radius = p.getOffset() - 1;
                int offset = p.getSize();
                if (radius > 5) radius = 5;
                if (offset == -1) offset = 0;
                Block center = p.getDispenser().getRelative(p.getTrain().getOppositeFace(), offset + 1);
                for (int i = 0 - radius; i <= radius; i++) {
                    for (int j = 0 - radius; j <= radius; j++) {
                        Block set = null;
                        if (p.getTrain() == BlockFace.EAST || p.getTrain() == BlockFace.WEST) {
                            set = center.getRelative(BlockFace.UP, i).getRelative(BlockFace.SOUTH, j);
                        } else if (p.getTrain() == BlockFace.NORTH || p.getTrain() == BlockFace.SOUTH) {
                            set = center.getRelative(BlockFace.UP, i).getRelative(BlockFace.EAST, j);
                        } else if (p.getTrain() == BlockFace.UP || p.getTrain() == BlockFace.DOWN) {
                            set = center.getRelative(BlockFace.SOUTH, i).getRelative(BlockFace.EAST, j);
                        }
                        if (set != null) {
                                set.setTypeId(newID, applyPhysics);
                                set.setData(newData, applyPhysics);
                        }
                    }
                }
            } else if (p.isLineEnabled()) {
                int length = p.getOffset() - 1;
                int offset = p.getSize();
                //System.out.println("l:" + length + " o:" + offset);
                for (int i = 0; i < length; i++) {
                    Block set = p.getDispenser().getRelative(p.getTrain().getOppositeFace(), i + offset + 2);
                        set.setTypeId(newID, applyPhysics);
                        set.setData(newData, applyPhysics);
                }
            } else {
                    existing.setTypeId(newID, applyPhysics);
                    existing.setData(newData, applyPhysics);
            }
        }
    }
}
