
package com.thevoxelbox.gadget.modifier;

import com.thevoxelbox.gadget.Processor;

public class OffsetModifier extends AbstractModifier{

    final private int offset;
    
    public OffsetModifier(int offset){
	this.offset = offset;
    }
    
    @Override
    public boolean modify(Processor p) {
	p.addOffset(offset);
	if(p.getOffset() > 100) p.setOffset(100);
	else if(p.getOffset() < 1) p.setOffset(1);
	return true;
    }

    
    
}
