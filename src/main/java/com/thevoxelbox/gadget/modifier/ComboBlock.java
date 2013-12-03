
package com.thevoxelbox.gadget.modifier;

import org.bukkit.Material;

public class ComboBlock {

    final private int id;
    final private byte data;
    
    public ComboBlock(int id){
	this(id, (byte)0);
    }
    
    public ComboBlock(int id, byte data){
	this.id = id;
	this.data = data;
    }
    
    public ComboBlock(Material mat){
	this(mat.getId());
    }
    
    public ComboBlock(Material mat, byte data){
	this(mat.getId(), data);
    }
    
    public int getID(){
	return id;
    }
    
    public byte getData(){
	return data;
    }
    
}
