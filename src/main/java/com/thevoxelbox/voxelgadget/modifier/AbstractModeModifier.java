
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

public abstract class AbstractModeModifier extends AbstractModifier{

    public boolean modeModify(Processor p){
	Block existing = p.dispenser.getRelative(p.train.getOppositeFace(),p.getOffset());
	if(p.filter == null) return modify(p);
	else if(existing.getTypeId() == p.filter.getTypeId() && existing.getData() == p.filter.getData()) return modify(p);
	else return true;
    }
    
    protected void setBlock(Block existing, int newID, byte newData, boolean applyPhysics, Processor p){
	Dispenser d = (Dispenser) p.dispenser.getState();
	if(newID == 46 && (p.override == null || p.override.getTypeId() != 46)){ //tnt; not overriden
	    if(!p.finite || processFinite(existing, newID, newData, d)) existing.getWorld().spawnEntity(existing.getLocation(), EntityType.PRIMED_TNT);
	    return;
	}
        if(existing.getState() instanceof InventoryHolder){
            if(newID == 0) return;
            InventoryHolder invBlock = (InventoryHolder) existing.getState();
            Inventory i = invBlock.getInventory();
	    if(i.firstEmpty() != -1 && (!p.finite || processFinite(existing, newID, newData, d))) i.addItem(new ItemStack(newID, 1, newData));
        }else{
	    if(p.areaEnabled){
		int radius = p.getOffset() - 1;
		int offset = p.getSize();
		if(radius > 5) radius = 5;
		if(offset == -1) offset = 0;
		Block center = p.dispenser.getRelative(p.train.getOppositeFace(), offset + 1);
		for(int i = 0 - radius; i <= radius; i++){
		    for(int j = 0 - radius; j <= radius; j++){
			Block set = null;
			if(p.train == BlockFace.EAST || p.train == BlockFace.WEST){
			    set = center.getRelative(BlockFace.UP, i).getRelative(BlockFace.SOUTH, j);
			}else if(p.train == BlockFace.NORTH || p.train == BlockFace.SOUTH){
			    set = center.getRelative(BlockFace.UP, i).getRelative(BlockFace.EAST, j);
			}else if(p.train == BlockFace.UP || p.train == BlockFace.DOWN){
			    set = center.getRelative(BlockFace.SOUTH, i).getRelative(BlockFace.EAST, j);
			}if(set != null){
			    if(!p.finite || processFinite(set, newID, newData, d)){
				set.setTypeId(newID, applyPhysics);
				set.setData(newData, applyPhysics); 
			    }
			}	
		    }
		}
	    }else if(p.lineEnabled){
		int length = p.getOffset() - 1;
		int offset = p.getSize();
		//System.out.println("l:" + length + " o:" + offset);
		for(int i = 0; i < length; i++){
		    Block set = p.dispenser.getRelative(p.train.getOppositeFace(), i + offset + 2);
		    if(!p.finite || processFinite(set, newID, newData, d)){
			set.setTypeId(newID, applyPhysics);
			set.setData(newData, applyPhysics); 
		    }
		}
	    }else{
		if(!p.finite || processFinite(existing, newID, newData, d)){
		    existing.setTypeId(newID, applyPhysics);
		    existing.setData(newData, applyPhysics); 
		}
	    }
        }
    }
    
    protected boolean processFinite(Block existing, int newID, byte newData, Dispenser disp){
	Inventory i = disp.getInventory();
	if(newID == 0){
	    HashMap<Integer, ItemStack> notAdded = i.addItem(new ItemStack(existing.getTypeId(), 1, existing.getData()));
	    return notAdded.isEmpty();
	}else{
	    if(existing.getTypeId() == newID && existing.getData() == newData) return false;
	    boolean success = true;
	    HashMap<Integer, ItemStack> notRemoved = i.removeItem(new ItemStack(newID, 1, newData));
	    if(!notRemoved.isEmpty()){
		success = false;
	    }if(existing.getTypeId() != 0 && !(existing.getState() instanceof InventoryHolder)){	
		HashMap<Integer, ItemStack> notAdded = i.addItem(new ItemStack(existing.getTypeId(), 1, existing.getData()));
		if(!notAdded.isEmpty()){
		    success = false;
		    i.addItem(new ItemStack(newID, 1, newData));
		}
	    }return success;
	}
    }
    
}
