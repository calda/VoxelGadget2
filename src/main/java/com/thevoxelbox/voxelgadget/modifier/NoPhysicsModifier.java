package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;

public class NoPhysicsModifier extends AbstractModifier {

    @Override
    public int modify(Processor p, Block nextBlock) {
        p.setApplyPhysics(false);
        return 0;
    }

}
