package com.thevoxelbox.voxelgadget.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GadgetCommand implements CommandExecutor {

	public final static String VOXEL_GADGET = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_PURPLE + "Voxel"
			+ ChatColor.DARK_AQUA + "Gadget" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
	private final SaveCommand save = new SaveCommand();
	private final OffsetCommand offset = new OffsetCommand();

	public boolean onCommand(CommandSender cs, Command cmnd, String label, String[] args) {
		if (!(cs instanceof Player)) {
			cs.sendMessage(VOXEL_GADGET + "You must be in-game to use this command.");
			return true;
		}
		Player p = (Player) cs;
		if (args.length == 0) sendCommandHelp(p);
		else if (args[0].equalsIgnoreCase("save")) save.onCommand(cs, cmnd, label, args);
		else if (args[0].equalsIgnoreCase("offset")) offset.onCommand(cs, cmnd, label, args);
		else sendCommandHelp(p);
		return true;
	}

	public static void sendCommandHelp(Player p) {
		p.sendMessage(VOXEL_GADGET + "Subcommands: ");
		p.sendMessage(VOXEL_GADGET + "/gadget save blueprintName (focus) (mode)");
		p.sendMessage(VOXEL_GADGET + "/gadget offset");
	}

}
