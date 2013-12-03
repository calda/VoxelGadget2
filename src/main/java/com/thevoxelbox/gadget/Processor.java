
package com.thevoxelbox.gadget;

import com.thevoxelbox.gadget.modifier.ComboBlock;
import com.thevoxelbox.gadget.modifier.ModifierType;
import java.util.HashMap;
import java.util.Map.Entry;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;

public class Processor {

    final HashMap<ModifierType, ComboBlock> config;
    final BlockFace[] faces = {BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH};
    
    int offset;
    ItemStack block;
    Block dispenser; 
    ModifierType mode;
    
    public Processor(HashMap<ModifierType, ComboBlock> config){
	this.config = config;
    }
    
    public boolean process(BlockDispenseEvent e){
	dispenser = e.getBlock();
	final BlockFace train;
	for(BlockFace face : faces){
	    Block possibleModeBlock = dispenser.getRelative(face);
	    ComboBlock possibleModeCombo = new ComboBlock(possibleModeBlock.getTypeId(), possibleModeBlock.getData());
	    ModifierType mode = getModifierFromConfig(possibleModeCombo);
	    if(mode != null){
		this.mode = mode;
		break;
	    }
	}
	return true;
    }
    
    public ModifierType getModifierFromConfig(ComboBlock block){
	for(Entry<ModifierType, ComboBlock> type : config.entrySet()){
	    if(type.getValue().equals(block)) return type.getKey();
	}return null;
    }
    
}
