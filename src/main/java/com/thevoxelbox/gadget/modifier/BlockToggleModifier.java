
package com.thevoxelbox.gadget.modifier;

import com.thevoxelbox.gadget.Processor;
import org.bukkit.block.Block;

public class BlockToggleModifier extends AbstractModifier{

    @Override
    public boolean modify(Processor p) {
        Block existing = p.dispenser.getRelative(p.train.getOppositeFace(), p.offset);
        if(existing.getType() == p.block.getType() && p.block.getData().getData() == existing.getData()){ //dispensed block
            if(p.override == null) setBlock(existing, 0, (byte)0, p.applyPhysics);
            else setBlock(existing, p.override.getTypeId(), (byte)p.override.getData(), p.applyPhysics);
        }else setBlock(existing, p.block.getTypeId(), (byte)p.block.getData().getData(), p.applyPhysics);
        return true;
    }
}
