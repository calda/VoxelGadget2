
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
    
    private int offset = 1;
    private int size = 0;
    public boolean areaEnabled = false;
    public boolean lineEnabled = false;
    public boolean finite = false;
    public ItemStack block;
    public Block override = null;
    public boolean overrideAbsolute = false;
    public Block filter = null;
    public Block dispenser; 
    public ModifierType mode;
    public BlockFace train = null;
    public boolean applyPhysics = true;
    public boolean filterAllowsToPlace = true;
    
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
		if(override != null){
		    ModifierType twoAgo = getModifierFromConfig(new ComboBlock(b.getRelative(train.getOppositeFace(), 2)));
		    if(twoAgo == ModifierType.OVERRIDE) overrideAbsolute = true;
		}else override = dispenser.getRelative(train, ++i);
	    }else if(modifier == ModifierType.FILTER){
                filter = dispenser.getRelative(train, ++i);
            }else if(modifier == ModifierType.AREA){
		areaEnabled = true;
		lineEnabled = false;
	    }else if(modifier == ModifierType.LINE){
		lineEnabled = true;
		areaEnabled = false;
	    }else if(modifier == ModifierType.FINITE){
		finite = true;
	    }else{
		boolean success = modifier.callModify(this);
	    }
	}return mode.callModeModify(this);
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
    
    public void addOffset(int add){
	if(areaEnabled || lineEnabled) addSize(add);
	else offset += add;
    }
    
    public void setOffset(int newOffset){
	offset = newOffset + 1;
    }
    
    public int getOffset(){
	return offset;
    }
    
    public void addSize(int add){
	size += add;
	if(size > 100) size = 100;
	else if(size < 1) size = 1;
    }
    
    public int getSize(){
	return size;
    }
    
}
