
package com.thevoxelbox.voxelgadget.command;

import static com.thevoxelbox.voxelgadget.command.GadgetCommand.VOXEL_GADGET;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class CopyCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender cs, Command cmnd, String label, String[] args) {
		if(!(cs instanceof Player)){
			cs.sendMessage(VOXEL_GADGET + "You must be in-game to run this command.");
			return true;
		}
		Player p = (Player) cs;
		ItemStack hand = p.getItemInHand();
		if(hand == null || hand.getType() != Material.WRITTEN_BOOK){
			p.sendMessage(VOXEL_GADGET + "You must have a Gadget Blueprint in your hand to use this command.");
			return true;
		}
		System.out.println(hand);
		BookMeta meta = (BookMeta) hand.getItemMeta();
		System.out.println(meta);
		String name = meta.getDisplayName();
		System.out.println(name);
		String newName = "Gadget Blueprint: Copy of ";
		if(name.contains("Copy of")) newName += name.substring(26);
		else newName += name.substring(18);
		meta.setDisplayName(newName);
		ItemStack newBook = new ItemStack(Material.WRITTEN_BOOK, 1);
		newBook.setItemMeta(meta);
		p.getInventory().addItem(newBook);
		p.sendMessage(VOXEL_GADGET + "Copied the Blueprint " + newName.substring(26));
		return true;
	}

}
