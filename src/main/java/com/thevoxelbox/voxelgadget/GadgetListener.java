package com.thevoxelbox.voxelgadget;

import static com.thevoxelbox.voxelgadget.VoxelGadget.log;
import static com.thevoxelbox.voxelgadget.command.GadgetCommand.VOXEL_GADGET;
import com.thevoxelbox.voxelgadget.command.SaveCommand;
import com.thevoxelbox.voxelgadget.modifier.ModifierType;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GadgetListener implements Listener {

	final VoxelGadget gadget;
	final TreeMap<Integer, ModifierType> config = new TreeMap<Integer, ModifierType>();

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
		if (e.getItem().getType().isBlock() || e.getItem().getTypeId() == 387) {
			Processor processor = new Processor(config, gadget);
			e.setCancelled(processor.process(e.getBlock(), e.getItem(), true));
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
			if (p.isSneaking()) index = 2;
			SaveCommand.addPoint(p.getName(), index, e.getClickedBlock().getLocation());
			p.sendMessage(VOXEL_GADGET + "Set point " + (index + 1));
			e.setCancelled(true);
		}
	}

	final private int CONFIG_VERSION = 4; //MUST UPDATE WHENEVER THE CONFIG IS CHANGED

	/**
	 * Loads the configuration for VoxelGadget2.
	 * If there is no config file, it is auto-generated.
	 */
	public void loadConfig() {
		File f = new File("plugins/VoxelGadget2/config.yml");
		if (f.exists()) {
			for (ModifierType type : ModifierType.values()) {
				String value = gadget.getConfig().getString(type.toString());
				String[] split;
				if (value != null && value.contains(":")) split = value.split(":");
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
					config.put(((id << 8) | data), type);
				} catch (NumberFormatException e) {
					log.warning("[VoxelGadget] There was an issue loading the configuration for the " + type + " modifier.");
					log.warning("[VoxelGadget] It has not been loaded into active modifier status.");
				}
			}
			log.info("[VoxelGadget] Config loaded");

			//if the config is out of date, update but preserve old values
			if (gadget.getConfig().getInt("CONFIG_VERSION") != CONFIG_VERSION) {
				if (gadget.getConfig().getBoolean("AUTOMATICALLY_UPDATE_CONFIG_ON_UPDATE")) {
					final HashMap<ModifierType, String> previousValues = new HashMap<ModifierType, String>();
					for (ModifierType type : ModifierType.values()) {
						String previous = gadget.getConfig().getString(type.toString());
						if (previous != null) {
							previousValues.put(type, previous);
						}
					}
					try {
						gadget.getConfig().save("plugins/VoxelGadget2/configV" + (CONFIG_VERSION - 1) + ".yml");
					} catch (IOException ex) {
						log.info("[VoxelGadget] Unable to copy old config first.");
					}
					f.delete();
					gadget.saveDefaultConfig();
					gadget.reloadConfig();
					for (ModifierType type : previousValues.keySet()) {
						gadget.getConfig().set(type.toString(), previousValues.get(type));
					}
					gadget.saveConfig();
					log.info("[VoxelGadget] Config Updated to CONFIG VERSION " + CONFIG_VERSION);
					log.info("[VoxelGadget] Your previous version of the config has been saved as configV" +(CONFIG_VERSION - 1) + ".yml");
				} else log.info("[VoxelGadget] CONFIG VERSION " + CONFIG_VERSION + " is avaliable but you have disabled automatic config updates.");
			}
		} else {
			for (ModifierType type : ModifierType.values()) {
				config.put(((type.getDefaultBlock().getID() << 8) | type.getDefaultBlock().getData()), type);
			}
			gadget.saveDefaultConfig();
			log.info("[VoxelGadget] Default config created");
		}
	}
}
