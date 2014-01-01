package com.thevoxelbox.voxelgadget.command;

import static com.thevoxelbox.voxelgadget.command.GadgetCommand.VOXEL_GADGET;
import com.thevoxelbox.voxelgadget.modifier.BlueprintHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class SaveCommand implements CommandExecutor {

	protected final static HashMap<String, Location[]> POINTS = new HashMap<String, Location[]>();
	public static int MAX_CUBOID_SIZE = 512;

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

	@Override
	public boolean onCommand(CommandSender cs, Command cmnd, String label, String[] args) {
		Player p = (Player) cs;
		if (args.length < 2) {
			p.sendMessage(VOXEL_GADGET + "/gadget save blueprintName (mode)");
			return true;
		}
		Location[] locs = POINTS.get(p.getName());
		if (locs == null || locs[0] == null || locs[1] == null) {
			p.sendMessage(VOXEL_GADGET + "You have not set a cuboid yet.");
			p.sendMessage(VOXEL_GADGET + "Set a cuboid using a book (id 340).");
			return true;
		}

		BlueprintHandler.StampMode mode = BlueprintHandler.StampMode.ALL;
		String name = args[1];
		if (args.length > 2) {
			String possibleMode = args[2];
			for(BlueprintHandler.StampMode m : BlueprintHandler.StampMode.values()) {
				if (m.toString().equalsIgnoreCase(possibleMode)) {
					mode = m;
					break;
				}
			}
		}

		ItemStack blueprintBook = createBlueprint(locs[0], locs[1], name, p.getName(), mode);
		List<String> bookLore = blueprintBook.getItemMeta().getLore();
		if (bookLore != null) {
			String errorMessage = bookLore.get(1);
			p.sendMessage(VOXEL_GADGET + "Unable to save '" + name + "' for reason:");
			p.sendMessage(VOXEL_GADGET + errorMessage);
		} else {
			p.getInventory().addItem(blueprintBook);
			p.sendMessage(VOXEL_GADGET + "A copy of '" + name + "' has been placed in your inventory.");
		}
		return true;
	}

	public static ItemStack createBlueprint(Location point1, Location point2, String name, String author, BlueprintHandler.StampMode mode) {
		//create book and open meta
		ItemStack stack = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta meta = (BookMeta) stack.getItemMeta();
		meta.setDisplayName("Gadget Blueprint: " + name);
		meta.setAuthor(author);

		try {
			//check cuboid size
			int dimX = Math.abs(point1.getBlockX() - point2.getBlockX()) + 1;
			int dimY = Math.abs(point1.getBlockY() - point2.getBlockY()) + 1;
			int dimZ = Math.abs(point1.getBlockZ() - point2.getBlockZ()) + 1;
			if (dimX * dimY * dimZ > MAX_CUBOID_SIZE) {
				throw new Exception("Cuboid size (" + (dimX * dimY * dimZ) + ") is not allowed. Maxium allowed size is " + MAX_CUBOID_SIZE);
			}
			//create book syntax
			ArrayList<String> book = new ArrayList<String>();
			book.add("BLUEPRINT " + name);
			book.add("*created by " + author);
			book.add("DIM " + dimX + "x" + dimY + "x" + dimZ);
			book.add("STAMP " + mode.toString());
			book.add("*BLUEPRINT BEGIN");
			//find North West point of cuboid selection
			Location lowest = (point1.getBlockY() > point2.getBlockY() ? point2 : point1);
			Location westMost = (point1.getBlockX() > point2.getBlockX() ? point2 : point1);
			Location northMost = (point1.getBlockZ() > point2.getBlockZ() ? point2 : point1);
			Location northWest = new Location(point1.getWorld(), westMost.getBlockX(), lowest.getBlockY(), northMost.getBlockZ());
			//load blocks into blueprint
			for (int y = 0; y < dimY; y++) {
				for (int z = 0; z < dimZ; z++) {
					StringBuilder line = new StringBuilder();
					line.append(y).append(",").append(z).append(">");
					for (int x = 0; x < dimX; x++) {
						Block b = point1.getWorld().getBlockAt(northWest.getBlockX() + x, northWest.getBlockY() + y, northWest.getBlockZ() + z);
						line.append(b.getTypeId());
						byte data = b.getData();
						if (data != (byte) 0) line.append(":").append(data);
						if (x != dimX - 1) line.append(";");
					}
					book.add(line.toString());
				}
			}
			book.add("*BLUEPRINT COMPLETE");
			//save contents to the book
			int numPages = (book.size() / 10) + (book.size() % 10 > 0 ? 1 : 0);
			if (numPages > 50) {
				throw new Exception("These dimentions (" + dimX + "," + dimY + "," + dimZ + ") would result in a book with more pages ("
						+ numPages + ") than the Minecraft-imposed maximum (50).");
			}
			for (int i = 0; i < numPages; i++) {
				int startingIndex = i * 10;
				StringBuilder bookPage = new StringBuilder();
				for (int j = startingIndex; j <= Math.min(startingIndex + 9, book.size() - 1); j++) {
					bookPage.append(book.get(j)).append("\n");
				}
				meta.addPage(bookPage.toString());
			}
			stack.setItemMeta(meta);
			return stack;
		} catch (Exception e) {
			String exception = e.getMessage();
			ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
			BookMeta bookMeta = (BookMeta) book.getItemMeta();
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("AN ERROR OCCURED:");
			lore.add(exception);
			bookMeta.setLore(lore);
			book.setItemMeta(bookMeta);
			return book;
		}
	}

}
