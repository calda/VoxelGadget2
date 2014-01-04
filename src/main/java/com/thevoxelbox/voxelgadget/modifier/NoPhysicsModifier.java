package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;

/**
 * @author CalDaBeast
 */
public class NoPhysicsModifier extends AbstractModifier {

    @Override
    public int modify(Processor p, Block currentBlock, Block nextBlock) {
        p.setApplyPhysics(false);
        return 0;
    }

}
