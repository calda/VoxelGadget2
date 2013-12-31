package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class PatchModifier extends AbstractModifier {

	@Override
	public int modify(Processor p, Block currentBlock, Block nextBlock) {
		if (nextBlock.getState() instanceof InventoryHolder) {
			if (nextBlock.getState() instanceof Dispenser) {
				for (BlockFace face : p.FACES) {
					if (p.getModifierFromConfig(new ComboBlock(nextBlock.getRelative(face))) == ModifierType.PATCH) {
						ModifierType behindDispenser = p.getModifierFromConfig(new ComboBlock(nextBlock.getRelative(face.getOppositeFace())));
						if (behindDispenser != null && behindDispenser.getType() == ModifierType.Type.MODE) {
							return 0;
						}
					}
				}
			}
			Inventory iBehind = ((InventoryHolder) nextBlock.getState()).getInventory();
			try {
				int offx = (iBehind.getItem(0) == null ? 0 : iBehind.getItem(0).getAmount() - 32);
				int offy = (iBehind.getItem(1) == null ? 0 : iBehind.getItem(1).getAmount() - 32);
				int offz = (iBehind.getItem(1) == null ? 0 : iBehind.getItem(2).getAmount() - 32);
				Location offsetTemp = new Location(p.getDispenser().getWorld(), offx, offy, offz);
				Block patchBlock = p.getDispenser().getRelative(p.getTail(), p.getCurrent());
				Block patchStart = patchBlock.getLocation().add(offsetTemp).getBlock();
				//System.out.println(patchStart.getType());
				BlockFace patchTail = null;
				if (ModifierType.PATCH.getDefaultBlock().equals(new ComboBlock(patchStart))) {
					for (BlockFace face : p.FACES) {
						ModifierType type = p.getModifierFromConfig(new ComboBlock(patchStart.getRelative(face)));
						if (type != null) {
							patchTail = face;
							//System.out.println("FOUND " + type + " AT " + face);
							break;
						}
					}
					if (patchTail == null) return 1;
					for (int current = 1; current <= 64; current++) {
						Block nextInTail = patchStart.getRelative(patchTail, current);
						ModifierType modifier = p.getModifierFromConfig(new ComboBlock(nextInTail));
						if (modifier == null) return 1;
						//System.out.println(modifier);
						if (modifier == ModifierType.PATCH) return 1;
						if (modifier.getType() != ModifierType.Type.CHECK) {
							int skip = modifier.callModify(p, nextInTail ,patchStart.getRelative(patchTail, current + 1));
							current += skip;
						}
					}
				} else return 1;
			} catch (NullPointerException e) {
				return 0;
			}
			return 1;
		}
		return 0;
	}

}
