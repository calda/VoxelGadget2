
package com.thevoxelbox.gadget.modifier;

import com.thevoxelbox.gadget.Processor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractModeModifier extends AbstractModifier{

    public boolean modeModify(Processor p){
	Block existing = p.dispenser.getRelative(p.train.getOppositeFace(),p.getOffset());
	if(p.filter == null) return modify(p);
	else if(existing.getTypeId() == p.filter.getTypeId() && existing.getData() == p.filter.getData()) return modify(p);
	else return true;
    }
    
    protected void setBlock(Block existing, int newID, byte newData, boolean applyPhysics, Processor p){
        if(existing.getState() instanceof InventoryHolder){
            if(newID == 0) return;
            InventoryHolder invBlock = (InventoryHolder) existing.getState();
            Inventory i = invBlock.getInventory();
	    boolean preExisting = false;
	    boolean containsEmptySlot = false;
            for(ItemStack item : i.getContents()){
                if(item == null) containsEmptySlot = true;
		else{
		    if(item.getTypeId() == newID && item.getData().getData() == newData && item.getAmount() != item.getMaxStackSize()){
			item.setAmount(item.getAmount() + 1);
			preExisting = true;
			break;
		    }
		}
            }if(!preExisting && containsEmptySlot){
		i.addItem(new ItemStack(newID, newData));
	    }
        }else{
	    if(p.areaEnabled){
		int radius = p.getSize();
		if(radius > 5) radius = 5;
		for(int i = 0 - radius; i <= radius; i++){
		    for(int j = 0 - radius; j <= radius; j++){
			Block set = null;
			if(p.train == BlockFace.EAST || p.train == BlockFace.WEST){
			    set = existing.getRelative(BlockFace.UP, i).getRelative(BlockFace.SOUTH, j);
			}else if(p.train == BlockFace.NORTH || p.train == BlockFace.SOUTH){
			    set = existing.getRelative(BlockFace.UP, i).getRelative(BlockFace.EAST, j);
			}else if(p.train == BlockFace.UP || p.train == BlockFace.DOWN){
			    set = existing.getRelative(BlockFace.SOUTH, i).getRelative(BlockFace.EAST, j);
			}if(set != null){
			    set.setTypeId(newID, applyPhysics);
			    set.setData(newData, applyPhysics); 
			}	
		    }
		}
	    }if(p.lineEnabled){
		int length = p.getOffset();
		int offset = p.getSize();
		for(int i = 0; i < length; i++){
		    Block set = p.dispenser.getRelative(p.train.getOppositeFace(), i + offset + 2);
		    set.setTypeId(newID, applyPhysics);
		    set.setData(newData, applyPhysics); 
		}
	    }else{
		existing.setTypeId(newID, applyPhysics);
		existing.setData(newData, applyPhysics); 
	    }
        }
    }
    
}
