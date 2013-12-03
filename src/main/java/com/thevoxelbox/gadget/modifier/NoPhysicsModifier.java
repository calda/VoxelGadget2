
package com.thevoxelbox.gadget.modifier;

import com.thevoxelbox.gadget.Processor;

public class NoPhysicsModifier extends AbstractModifier{

    @Override
    public boolean modify(Processor p) {
	p.applyPhysics = false;
	return true;
    }
    
}
