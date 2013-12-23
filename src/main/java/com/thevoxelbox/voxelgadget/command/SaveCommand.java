package com.thevoxelbox.voxelgadget.command;

import static com.thevoxelbox.voxelgadget.command.GadgetCommand.VOXEL_GADGET;
import com.thevoxelbox.voxelgadget.modifier.BlueprintModifier;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class SaveCommand implements CommandExecutor {

	protected final static HashMap<String, Location[]> POINTS = new HashMap<String, Location[]>();

	public static void addPoint(String name, int index, Location point) {
		if (!POINTS.containsKey(name)) {
			Location[] locs = new Location[3];
			locs[index] = point;
			POINTS.put(name, locs);
		} else {
			Location[] locs = POINTS.get(name);
			locs[index] = point;
			POINTS.put(name, locs);
		}
	}

	public boolean onCommand(CommandSender cs, Command cmnd, String label, String[] args) {
		Player p = (Player) cs;
		if (args.length < 1) {
			p.sendMessage(VOXEL_GADGET + "/gadget save blueprintName (mode)");
			return true;
		}
		Location[] locs = POINTS.get(p.getName());
		if (locs == null || locs[0] == null || locs[1] == null) {
			p.sendMessage(VOXEL_GADGET + "You have not set a cuboid yet.");
			p.sendMessage(VOXEL_GADGET + "Set a cuboid using a book (id 340).");
			return true;
		}
		int dimX = Math.abs(locs[0].getBlockX() - locs[1].getBlockX()) + 1;
		int dimY = Math.abs(locs[0].getBlockY() - locs[1].getBlockY()) + 1;
		int dimZ = Math.abs(locs[0].getBlockZ() - locs[1].getBlockZ()) + 1;
		if (dimX * dimY * dimZ > 125) {
			p.sendMessage(VOXEL_GADGET + "Cuboid exceeds maximum size.");
			p.sendMessage(VOXEL_GADGET + "Maximum volume is 125 blocks (5x5x5).");
			p.sendMessage(VOXEL_GADGET + "Your current volume is " + (dimX * dimY * dimZ) + " blocks ("
					+ dimX + "x" + dimY + "x" + dimZ + ").");
			return true;
		}
		BlueprintModifier.OverrideMode mode = BlueprintModifier.OverrideMode.ALL;
		String name = args[1];
		for (String arg : args) {
			for (BlueprintModifier.OverrideMode m : BlueprintModifier.OverrideMode.values()) {
				if (m.toString().equalsIgnoreCase(arg)) {
					mode = m;
					break;
				}
			}
		}

		ItemStack stack = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta meta = (BookMeta) stack.getItemMeta();
		meta.setDisplayName("Gadget Blueprint: " + name);
		meta.setAuthor(p.getName());

		//create book syntax
		ArrayList<String> book = new ArrayList<String>();
		book.add("BLUEPRINT " + name);
		book.add("*created by " + p.getName());
		book.add("DIM " + dimX + "x" + dimY + "x" + dimZ);
		book.add("REPLACE " + mode.toString());

		Location lowest = (locs[0].getBlockY() > locs[1].getBlockY() ? locs[1] : locs[0]);
		Location westMost = (locs[0].getBlockX() > locs[1].getBlockX() ? locs[1] : locs[0]);
		Location northMost = (locs[0].getBlockZ() > locs[1].getBlockZ() ? locs[1] : locs[0]);
		Location northWest = new Location(locs[1].getWorld(), westMost.getBlockX(), lowest.getBlockY(), northMost.getBlockZ());

		//System.out.println(northWest);

		for (int y = 0; y < dimY; y++) {
			book.add("\n");
			for (int z = 0; z < dimZ; z++) {
				StringBuilder line = new StringBuilder();
				for (int x = 0; x < dimX; x++) {
					Block b = locs[1].getWorld().getBlockAt(northWest.getBlockX() + x, northWest.getBlockY() + y, northWest.getBlockZ() + z);
					line.append(b.getTypeId());
					byte data = b.getData();
					if (data != (byte) 0) line.append(":").append(data);
					if (x != dimX - 1) line.append(";");
				}
				book.add(line.toString());
			}
		}

		int numPages = (book.size() / 10) + (book.size() % 10 > 0 ? 1 : 0);
		for (int i = 0; i < numPages; i++) {
			int startingIndex = i * 10;
			StringBuilder bookPage = new StringBuilder();
			//System.out.println("Page " + i + " : " + startingIndex + " - " + Math.min(startingIndex + 9, book.size() - 1));
			for (int j = startingIndex; j <= Math.min(startingIndex + 9, book.size() - 1); j++) {
				//System.out.println("Page " + i + " line " + (j - startingIndex + 1) + ": " + book.get(j));
				bookPage.append(book.get(j)).append("\n");
			}
			meta.addPage(bookPage.toString());
		}

		stack.setItemMeta(meta);
		p.getInventory().addItem(stack);
		p.sendMessage(VOXEL_GADGET + "Created " + name + " blueprint with dimentions of "
				+ dimX + "x" + dimY + "x" + dimZ + " and override mode " + mode);
		return true;
	}

}
