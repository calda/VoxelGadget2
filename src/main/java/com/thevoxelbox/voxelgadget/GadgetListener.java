package com.thevoxelbox.voxelgadget;

import static com.thevoxelbox.voxelgadget.VoxelGadget.log;
import static com.thevoxelbox.voxelgadget.command.GadgetCommand.VOXEL_GADGET;
import com.thevoxelbox.voxelgadget.command.SaveCommand;
import com.thevoxelbox.voxelgadget.modifier.ComboBlock;
import com.thevoxelbox.voxelgadget.modifier.ModifierType;
import java.io.File;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GadgetListener implements Listener {

	final VoxelGadget gadget;
	final HashMap<ModifierType, ComboBlock> config = new HashMap<ModifierType, ComboBlock>();
	private boolean infiniteBlocks = true;

	public GadgetListener(VoxelGadget gadget) {
		this.gadget = gadget;
	}

	/**
	 * Calls VoxelGadget dispenser logic
	 *
	 * @param e Bukkit's BlockDispenseEvent
	 */
	@EventHandler
	public void onDispenserDispense(BlockDispenseEvent e) {
		if (e.getItem().getType().isBlock()) {
			Processor processor = new Processor(config, infiniteBlocks, gadget);
			boolean success = processor.process(e.getBlock(), e.getItem(), true);
			e.setCancelled(success);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getMaterial() == Material.BOOK) {
			int index = -1;
			if (e.getAction() == Action.LEFT_CLICK_BLOCK) index = 0;
			else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) index = 1;
			if (index == -1) return;
			Player p = e.getPlayer();
			SaveCommand.addPoint(p.getName(), index, e.getClickedBlock().getLocation());
			p.sendMessage(VOXEL_GADGET + "Set point " + (index + 1));
		}
	}

	/**
	 * Loads the configuration for VoxelGadget2.
	 * If there is no config file, it is auto-generated.
	 */
	public void loadConfig() {
		File f = new File("plugins/VoxelGadget/config.yml");
		if (f.exists()) {
			for (ModifierType type : ModifierType.values()) {
				String value = gadget.getConfig().getString(type.toString());
				String[] split;
				if (value.contains(":")) split = value.split(":");
				else {
					split = new String[2];
					split[0] = value;
					split[1] = "0";
				}
				int id;
				int data;
				try {
					id = Integer.parseInt(split[0]);
					data = Integer.parseInt(split[1]);
					ComboBlock combo = new ComboBlock(id, (byte) data);
					config.put(type, combo);
				} catch (NumberFormatException e) {
					log.warning("[VoxelGadget] There was an issue loading the configuration for the " + type + " modifier.");
					log.warning("[VoxelGadget] It has not been loaded into active modifier status.");
				}
			}
			if (gadget.getConfig().contains("INFINITE_BLOCKS")) {
				infiniteBlocks = gadget.getConfig().getBoolean("INFINITE_BLOCKS");
			}
			log.info("[VoxelGadget] Config loaded");
		} else {
			for (ModifierType type : ModifierType.values()) {
				config.put(type, type.getDefaultBlock());
			}
			gadget.saveDefaultConfig();
			log.info("[VoxelGadget] Default config created");
		}
	}
}
