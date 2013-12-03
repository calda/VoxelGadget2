
package com.thevoxelbox.gadget.modifier;

import com.thevoxelbox.gadget.Processor;

public class OffsetModifier extends AbstractModifier{

    final private int offset;
    
    public OffsetModifier(int offset){
	this.offset = offset;
    }
    
    @Override
    public boolean modify(Processor p) {
	p.offset += offset;
	if(p.offset > 100) p.offset = 100;
	else if(p.offset < 0) p.offset = 0;
	System.out.println("offset = " + p.offset);
	return true;
    }

    
    
}
