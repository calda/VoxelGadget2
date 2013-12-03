
package com.thevoxelbox.gadget.modifier;

import com.thevoxelbox.gadget.Processor;
import org.bukkit.block.Block;

public class BlockToggleModifier extends AbstractModifier{

    @Override
    public boolean modify(Processor p) {
        Block existing = p.dispenser.getRelative(p.train.getOppositeFace(), p.offset);
        if(existing.getType() == p.block.getType() && p.block.getData().getData() == existing.getData()){ //dispensed block
            if(p.override == null){
                existing.setTypeId(0, p.applyPhysics);
                existing.setData((byte)0, p.applyPhysics);
            }else{
                existing.setTypeId(p.override.getTypeId(), p.applyPhysics);
                existing.setData(p.override.getData(), p.applyPhysics);
            }
        }else{
            existing.setTypeId(p.block.getTypeId(), p.applyPhysics);
            existing.setData(p.block.getData().getData(), p.applyPhysics); 
        }
    
        return true;
    }
    
}
