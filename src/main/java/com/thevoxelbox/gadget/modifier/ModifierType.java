
package com.thevoxelbox.gadget.modifier;

public enum ModifierType {
    PLACE(null, Type.MODE, null),
    TOGGLE(null, Type.MODE, null),
    REMOVE(null, Type.MODE, null),
    SUCKER(null, Type.MODE, null),
    AREA(null, Type.SPECIAL, null),
    ADD1(null, Type.OFFSET, null),
    ADD5(null, Type.OFFSET, null),
    ADD10(null, Type.OFFSET, null),
    ADD25(null, Type.OFFSET, null),
    SUBTRACT1(null, Type.OFFSET, null),
    SUBTRACT5(null, Type.OFFSET, null);
    
    private final Class c;
    private final ComboBlock defaultBlock;
    
    private ModifierType(Class c, Type type, ComboBlock defaultBlock){
	this.c = c;
	this.defaultBlock = defaultBlock;
    }
    
    public Class getClassLocation(){
	return c;
    }
    
    public ComboBlock getDefaultBlock(){
	return defaultBlock;
    }
    
    public enum Type{
	MODE, OFFSET, SPECIAL;
    }
    
}
