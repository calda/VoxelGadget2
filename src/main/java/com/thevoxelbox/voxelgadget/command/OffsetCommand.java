package com.thevoxelbox.voxelgadget.command;

import static com.thevoxelbox.voxelgadget.command.GadgetCommand.VOXEL_GADGET;
import static com.thevoxelbox.voxelgadget.command.SaveCommand.POINTS;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class OffsetCommand implements CommandExecutor {

	public boolean onCommand(CommandSender cs, Command cmnd, String label, String[] args) {
		Player p = (Player) cs;

		Location[] locs = POINTS.get(p.getName());
		if (locs == null || locs[0] == null || locs[1] == null) {
			p.sendMessage(VOXEL_GADGET + "You have not set a cuboid yet.");
			p.sendMessage(VOXEL_GADGET + "Set a cuboid using a book (id 340).");
			return true;
		}

		Vector v1 = locs[0].toVector();
		Vector v2 = locs[1].toVector();
		Vector sub = v1.subtract(v2);
		int zero = 0;
		int notZero = 0;
		if (sub.getBlockX() == 0) zero++;
		else notZero = sub.getBlockX();
		if (sub.getBlockY() == 0) zero++;
		else notZero = sub.getBlockY();
		if (sub.getBlockZ() == 0) zero++;
		else notZero = sub.getBlockZ();
		p.sendMessage(VOXEL_GADGET + "Offest from Point 1 to Point 2:");
		p.sendMessage(VOXEL_GADGET + ChatColor.DARK_GRAY + "(" + (sub.getBlockX() + 32) + ", " + (sub.getBlockX() + 32) + ", " + (sub.getBlockX() + 32) + ")"
				+ (zero >= 2 ? " or a standard offset of " + (notZero - 1) : ""));

		return true;
	}

}
