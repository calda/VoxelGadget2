
package com.thevoxelbox.gadget.modifier;

import com.thevoxelbox.gadget.Processor;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

class InventoryModifier extends AbstractModifier {

    @Override
    public boolean modify(Processor p){
	ArrayList<ItemStack> similars = new ArrayList<ItemStack>();
	Block dispenser = p.dispenser;
	Dispenser disp = (Dispenser) dispenser.getState();
	Inventory inv = disp.getInventory();
	for(ItemStack item : inv.getContents()){
	    if(item != null && item.getTypeId() == p.block.getTypeId() && item.getData().getData() == p.block.getData().getData()){
		similars.add(item);
	    }
	}Random r = new Random();
	System.out.println(similars);
	p.offset = similars.get(r.nextInt(similars.size())).getAmount();
	System.out.println(p.offset);
	return true;
    }

}
