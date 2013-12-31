package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

class InventoryModifier extends AbstractModifier {

    @Override
    public int modify(Processor p, Block currentBlock, Block nextBlock) {
        ArrayList<ItemStack> similars = new ArrayList<ItemStack>();
        Block dispenser = p.getDispenser();
        Dispenser disp = (Dispenser) dispenser.getState();
        Inventory inv = disp.getInventory();
        for (ItemStack item : inv.getContents()) {
            if (item != null && item.getTypeId() == p.getDispensed().getTypeId() && item.getData().getData() == p.getDispensed().getData().getData()) {
                similars.add(item);
            }
        }
        Random r = new Random();
        p.setOffset(similars.get(r.nextInt(similars.size())).getAmount() + 1);
        return 0;
    }

}
