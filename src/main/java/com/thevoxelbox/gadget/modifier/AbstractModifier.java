
package com.thevoxelbox.gadget.modifier;

import com.thevoxelbox.gadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractModifier {

    public abstract boolean modify(Processor p);
    
    protected void setBlock(Block existing, int newID, byte newData, boolean applyPhysics){
        if(existing.getState() instanceof InventoryHolder){
            if(newID == 0) return;
            InventoryHolder invBlock = (InventoryHolder) existing.getState();
            Inventory i = invBlock.getInventory();
            for(ItemStack item : i.getContents()){
                //contains air stacks or just contains filled items?
            }
        }else{
            existing.setTypeId(newID, applyPhysics);
            existing.setData(newData, applyPhysics); 
        }
    }
    
}
