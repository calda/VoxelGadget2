
package com.thevoxelbox.gadget.modifier;

import com.thevoxelbox.gadget.Processor;
import org.bukkit.Material;

public enum ModifierType {
    PLACE(new BlockPlaceModifier(), Type.MODE, new ComboBlock(Material.IRON_BLOCK)),
    TOGGLE(new BlockToggleModifier(), Type.MODE, new ComboBlock(Material.LAPIS_BLOCK)),
    REMOVE(new BlockRemoveModifier(), Type.MODE, new ComboBlock(Material.DIAMOND_BLOCK)),
    SUCKER(null, Type.MODE, new ComboBlock(Material.GOLD_BLOCK)),
    NO_PHYSICS(new NoPhysicsModifier(), Type.SPECIAL, new ComboBlock(Material.WOOL, (byte)8)),
    OVERRIDE(null, Type.SPECIAL, new ComboBlock(Material.WOOL, (byte)14)),
    INVENTORY(new InventoryModifier(), Type.SPECIAL, new ComboBlock(Material.WOOL, (byte)12)),
    FINITE_TOGGLE(null, Type.SPECIAL, new ComboBlock(Material.WOOL, (byte)13)),
    LINE(null, Type.SPECIAL, new ComboBlock(Material.WOOL, (byte)2)),
    AREA(null, Type.SPECIAL, new ComboBlock(Material.WOOL, (byte)1)),
    FILTER(null, Type.SPECIAL, new ComboBlock(Material.WOOL, (byte)4)),
    SKIP(null, Type.SPECIAL, new ComboBlock(Material.WOOL, (byte)0)),
    DUMMY(null, Type.SPECIAL, new ComboBlock(Material.WOOL, (byte)6)),
    ADD1(new OffsetModifier(1), Type.OFFSET, new ComboBlock(Material.WOOL, (byte)9)),
    ADD5(new OffsetModifier(5), Type.OFFSET, new ComboBlock(Material.WOOL, (byte)11)),
    ADD10(new OffsetModifier(10), Type.OFFSET, new ComboBlock(Material.WOOL, (byte)10)),
    ADD25(new OffsetModifier(25), Type.OFFSET, new ComboBlock(Material.WOOL, (byte)3)),
    SUBTRACT1(new OffsetModifier(-1), Type.OFFSET, new ComboBlock(Material.WOOL, (byte)5)),
    SUBTRACT5(new OffsetModifier(-5), Type.OFFSET, new ComboBlock(Material.WOOL, (byte)15));
    
    private final AbstractModifier modifier;
    private final ComboBlock defaultBlock;
    private final Type type;
    
    private ModifierType(AbstractModifier modifier, Type type, ComboBlock defaultBlock){
	this.modifier = modifier;
	this.defaultBlock = defaultBlock;
	this.type = type;
    }
    
    public boolean callModify(Processor p){
	if(modifier != null) return modifier.modify(p);
	return true;
    }
    
    public Type getType(){
	return type;
    }
    
    public ComboBlock getDefaultBlock(){
	return defaultBlock;
    }
    
    public enum Type{
	MODE, OFFSET, SPECIAL;
    }
    
}
