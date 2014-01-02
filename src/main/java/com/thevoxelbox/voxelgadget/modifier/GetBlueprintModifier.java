package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import com.thevoxelbox.voxelgadget.command.SaveCommand;
import org.bukkit.Location;
import org.bukkit.block.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class GetBlueprintModifier extends AbstractModeModifier {

	@Override
	public int modify(Processor p, Block currentBlock, Block behind1) {
		Block behind2 = currentBlock.getRelative(currentBlock.getFace(behind1), 2);
		Block behind3 = currentBlock.getRelative(currentBlock.getFace(behind1), 3);
		if (behind1.getState() instanceof InventoryHolder) {
			if (behind2.getState() instanceof InventoryHolder) {
				if (behind3.getState() instanceof InventoryHolder) {
					p.setMode(ModifierType.GET_BLUEPRINT);
					p.setInvOverride(((InventoryHolder) behind1.getState()).getInventory());
					return 3;
				} else return 2;
			} else return 1;
		} else return 0;
	}

	@Override
	public int modeModify(Processor p) {
		Inventory inv1 = p.getInvOverride();
		Block invBlock1 = ((BlockState) inv1.getHolder()).getBlock();
		BlockFace otherInvFace = null;
		for (BlockFace face : Processor.FACES) {
			if (p.getModifierFromConfig(new ComboBlock(invBlock1.getRelative(face))) == ModifierType.GET_BLUEPRINT) {
				if (invBlock1.getRelative(face.getOppositeFace()).getState() instanceof InventoryHolder) {
					otherInvFace = face.getOppositeFace();
					break;
				}
			}
		}
		if (otherInvFace == null) return 0;
		Inventory inv2 = ((InventoryHolder) invBlock1.getRelative(otherInvFace, 1).getState()).getInventory();
		Inventory inv3 = ((InventoryHolder) invBlock1.getRelative(otherInvFace, 2).getState()).getInventory();
		Location cuboid1 = Offset3DModifier.getOffsetFromInventory(inv2, p);
		Location cuboid2 = Offset3DModifier.getOffsetFromInventory(inv3, p);
		ItemStack book = SaveCommand.createBlueprint(cuboid1, cuboid2, "BLUEPRINT", "Get Blueprint", BlueprintHandler.StampMode.ALL);
		inv1.clear();
		inv1.addItem(book);
		return 0;
	}

}
