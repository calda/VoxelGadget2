
package com.thevoxelbox.gadget;

import com.thevoxelbox.gadget.modifier.ComboBlock;
import com.thevoxelbox.gadget.modifier.ModifierType;
import java.util.HashMap;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;

public class Processor {

    final HashMap<ModifierType, ComboBlock> config;
    final BlockFace[] faces = {BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH};
    
    public int offset;
    public ItemStack block;
    public Block override = null;
    public Block dispenser; 
    public ModifierType mode;
    public BlockFace train = null;
    public boolean applyPhysics = true;
    
    public Processor(HashMap<ModifierType, ComboBlock> config){
	this.config = config;
    }
    
    public boolean process(BlockDispenseEvent e){
	System.out.println("Dispense Event called;");
	dispenser = e.getBlock();
	block = e.getItem();
	for(BlockFace face : faces){
	    Block possibleModeBlock = dispenser.getRelative(face);
	    ComboBlock possibleModeCombo = new ComboBlock(possibleModeBlock.getTypeId(), possibleModeBlock.getData());
	    ModifierType mode = getModifierFromConfig(possibleModeCombo);
	    if(mode != null && mode.getType() == ModifierType.Type.MODE){
		this.mode = mode;
		train = face;
		break;
	    }
	}if(train == null) return false;
	System.out.println(train);
	for(int i = 2; i < 64; i++){
	    Block b = dispenser.getRelative(train, i);
	    ModifierType modifier = getModifierFromConfig(new ComboBlock(b));
	    System.out.println(new ComboBlock(b) + " -> " + modifier + " at " + i + " " + train);
	    if(modifier == null) break;
	    if(modifier == ModifierType.SKIP) i++;
	    else if(modifier == ModifierType.OVERRIDE){
		override = dispenser.getRelative(train, ++i);
	    }else{
		boolean success = modifier.callModify(this);
	    }
	}return mode.callModify(this);
    }
    
    public ModifierType getModifierFromConfig(ComboBlock block){
	for(Entry<ModifierType, ComboBlock> type : config.entrySet()){
	    if(type.getValue().id == block.id){
		if(block.id == Material.WOOL.getId()){
		    if(type.getValue().data == block.data) return type.getKey();
		}else return type.getKey();
	    }
	}return null;
    }
    
}
