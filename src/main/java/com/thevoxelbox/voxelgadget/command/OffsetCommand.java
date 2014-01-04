package com.thevoxelbox.voxelgadget.command;

import static com.thevoxelbox.voxelgadget.command.GadgetCommand.VOXEL_GADGET;
import static com.thevoxelbox.voxelgadget.command.SaveCommand.POINTS;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * @author CalDaBeast
 */
public class OffsetCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmnd, String label, String[] args) {
		Player p = (Player) cs;

		Location[] locs = POINTS.get(p.getName());
		if (locs == null || locs[0] == null || locs[1] == null) {
			p.sendMessage(VOXEL_GADGET + "You have not set a cuboid yet.");
			p.sendMessage(VOXEL_GADGET + "Set a cuboid using a book (id 340).");
			return true;
		}

		Vector v1 = locs[1].toVector();
		Vector v2 = locs[0].toVector();
		Vector sub = v1.subtract(v2);
		int zero = 0;
		int notZero = 0;
		if (sub.getBlockX() == 0) zero++;
		else notZero = sub.getBlockX();
		if (sub.getBlockY() == 0) zero++;
		else notZero = sub.getBlockY();
		if (sub.getBlockZ() == 0) zero++;
		else notZero = sub.getBlockZ();
		if(notZero > 1) notZero--;
		else if (notZero < 1) notZero++;
		p.sendMessage(VOXEL_GADGET + "Offest from Point 1 to Point 2:");
		p.sendMessage(VOXEL_GADGET + "(" + (sub.getBlockX() + 32) + ", " + (sub.getBlockY() + 32) + ", " + (sub.getBlockZ() + 32) + ")"
				+ (zero >= 2 ? " or a standard offset of " + Math.abs(notZero) : ""));

		if (args.length > 1 && args[1].equalsIgnoreCase("save")) {
			Location loc3 = locs[2];
			if(loc3 == null){
				p.sendMessage(VOXEL_GADGET + "You must select a Point 3 to save the offset.");
				p.sendMessage(VOXEL_GADGET + "Set Point 3 by punching a block with a book while sneaking.");
				return false;
			}
			Block target = locs[2].getBlock();
			if (!(target.getState() instanceof InventoryHolder)) {
				p.sendMessage(VOXEL_GADGET + "The offset was not saved because the target block does not contain an inventory.");
				return false;
			}
			Inventory i = ((InventoryHolder) target.getState()).getInventory();
			if (i.getSize() < 3) {
				p.sendMessage(VOXEL_GADGET + "The offset was not saved because the target inventory is not large enough.");
				return false;
			}
			ItemStack[] xyz = new ItemStack[3];
			xyz[0] = new ItemStack(1);
			xyz[1] = new ItemStack(2);
			xyz[2] = new ItemStack(3);
			if (args.length >= 3 && args.length <= 5) {
				for (int j = 0; j <= 2; j++) {
					String value = args[j + 2];
					String[] split;
					if (value != null && value.contains(":")) split = value.split(":");
					else {
						split = new String[2];
						split[0] = value;
						split[1] = "0";
					}
					int id;
					byte data;
					try {
						id = Integer.parseInt(split[0]);
						data = Byte.parseByte(split[1]);
						xyz[j] = new ItemStack(id, 32, data);
					} catch (NumberFormatException e) {
						p.sendMessage(VOXEL_GADGET + value + " is not a valid item ID.");
						return false;
					}
				}
			}
			xyz[0].setAmount(sub.getBlockX() + 32);
			xyz[1].setAmount(sub.getBlockY() + 32);
			xyz[2].setAmount(sub.getBlockZ() + 32);
			i.setContents(xyz);
			p.sendMessage(VOXEL_GADGET + "The offset was saved in the target inventory" + (args.length >= 3 && args.length <= 5 ? " with the specified item IDs." : "."));
		}
		return true;
	}

}
