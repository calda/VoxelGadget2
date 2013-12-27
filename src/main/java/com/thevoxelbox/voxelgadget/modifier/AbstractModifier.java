package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;

public abstract class AbstractModifier {

    /**
     * Modifies the Processor based on the Modifier's design
     * @param p the Processor to modify
	 * @param nextBlock the following block in the tail of the Gadget
     * @return The number of blocks that should be skipped in the tail of the Gadget
     */
    public abstract int modify(Processor p, Block nextBlock);

}
