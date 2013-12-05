
package com.thevoxelbox.gadget.modifier;

import com.thevoxelbox.gadget.Processor;
import java.util.HashMap;
import java.util.List;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class BlockSuckerMode extends AbstractModeModifier{

    @Override
    public boolean modify(Processor p) {
	Dispenser d = (Dispenser) p.dispenser.getState();
	List<Entity> near = p.dispenser.getLocation().getWorld().getEntities();
	for(Entity e : near) {
	    if(e.getLocation().distance(d.getLocation()) <= 10){
		if(e.getType() == EntityType.DROPPED_ITEM){
		    Item i = (Item) e;
		    HashMap<Integer, ItemStack> notRemoved = d.getInventory().addItem(new ItemStack(i.getItemStack().getTypeId(), i.getItemStack().getAmount(), i.getItemStack().getData().getData()));
		    if(!notRemoved.isEmpty()){
			i.setItemStack(notRemoved.entrySet().iterator().next().getValue());
		    }else i.remove();
		}
	    }
	}return true;
    }

}