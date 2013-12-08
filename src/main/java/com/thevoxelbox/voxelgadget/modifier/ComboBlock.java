package com.thevoxelbox.voxelgadget.modifier;

import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * A combined class to store the ID and data/ink of a block
 */
public class ComboBlock {

    final private int id;
    final private byte data;

    public ComboBlock(int id) {
        this(id, (byte) 0);
    }

    public ComboBlock(int id, byte data) {
        this.id = id;
        this.data = data;
    }

    public ComboBlock(Block block) {
        this(block.getTypeId(), block.getData());
    }

    public ComboBlock(Material mat) {
        this(mat.getId());
    }

    public ComboBlock(Material mat, byte data) {
        this(mat.getId(), data);
    }

    public int getID() {
        return id;
    }

    public byte getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ComboBlock[id=" + id + "; data=" + data + "]";
    }

}
