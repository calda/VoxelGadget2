package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;

public abstract class AbstractModifier {

    /**
     * Modifies the Processor based on the Modifier's design
     * @param p the Processor to modify
     * @return true if successful
     */
    public abstract int modify(Processor p, Block nextBlock);

}
