
package com.thevoxelbox.gadget.modifier;

import com.thevoxelbox.gadget.Processor;
import org.bukkit.block.Block;

public class BlockPlaceModifier extends AbstractModifier{

    @Override
    public boolean modify(Processor p) {
	Block change = p.dispenser.getRelative(p.train.getOppositeFace(), p.offset + 1);
	if(p.override == null){
	    change.setTypeId(p.block.getTypeId(), p.applyPhysics);
	    change.setData(p.block.getData().getData(), p.applyPhysics);
	}else{
	    change.setTypeId(p.override.getTypeId(), p.applyPhysics);
	    change.setData(p.override.getData(), p.applyPhysics);
	}return true;
    }

}
