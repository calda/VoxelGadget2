package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class GetBlockModifier extends AbstractModeModifier {

    @Override
    public int modify(Processor p, Block behind) {
        p.setMode(ModifierType.GET_BLOCK);
        if (behind.getState() instanceof InventoryHolder) {
            p.setInvOverride(((InventoryHolder) behind.getState()).getInventory());
            return 1;
        }
        return 0;
    }

    @Override
    public int modeModify(Processor p) {
        Block existing = p.getDispenser().getRelative(p.getTail().getOppositeFace(), p.getOffset());
        if (p.getOffset3D() != null) {
            existing = p.getOffset3D().getBlock();
		}
		final Inventory inv = ((p.getInvOverride() == null) ? ((Dispenser) p.getDispenser().getState()).getInventory() : p.getInvOverride());
		inv.clear();
		if(existing.getType() != Material.AIR) inv.addItem(new ItemStack(existing.getType(), 1, existing.getData()));
        return 0;
    }
}
