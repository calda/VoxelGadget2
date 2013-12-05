package com.thevoxelbox.gadget;

import static com.thevoxelbox.gadget.VoxelGadget.log;
import com.thevoxelbox.gadget.modifier.ComboBlock;
import com.thevoxelbox.gadget.modifier.ModifierType;
import java.io.File;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

public class GadgetListener implements Listener{
    
    final static String gadgetText = ChatColor.DARK_GRAY + "[" + ChatColor.LIGHT_PURPLE + "Voxel" + 
	    ChatColor.DARK_PURPLE + "Gadget" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
    final VoxelGadget gadget;
    final HashMap<ModifierType, ComboBlock> config = new HashMap<ModifierType, ComboBlock>();
    private boolean infiniteBlocks = true;
    
    public GadgetListener(VoxelGadget gadget){
	this.gadget = gadget;
    }
    
    @EventHandler
    public void onDispenserDispense(BlockDispenseEvent e){
	if(e.getItem().getType().isBlock()){
	    Processor processor = new Processor(config, infiniteBlocks, gadget);
	    boolean success = processor.process(e.getBlock(), e.getItem(), true);
	    e.setCancelled(success);
	}
    }
    
    public void loadConfig() {
            File f = new File("plugins/VoxelGadget/config.yml");
            if (f.exists()){
		for(ModifierType type : ModifierType.values()){
		    String value = gadget.getConfig().getString(type.toString());
		    String[] split;
		    if(value.contains(":")) split = value.split(":");
		    else{
			split = new String[2];
			split[0] = value;
			split[1] = "0";
		    }int id;
		    int data;
		    try{
			id = Integer.parseInt(split[0]);
			data = Integer.parseInt(split[1]);
			ComboBlock combo = new ComboBlock(id, (byte)data);
			config.put(type, combo);
		    }catch(NumberFormatException e){
			log.warning("[VoxelGadget] There was an issue loading the configuration for the " + type + " modifier.");
			log.warning("[VoxelGadget] It has not been loaded into active modifier status.");
		    }
		}if(gadget.getConfig().contains("INFINITE_BLOCKS")){
		    infiniteBlocks = gadget.getConfig().getBoolean("INFINITE_BLOCKS");
		}
		log.info("[VoxelGadget] Config loaded");
            }else{
		for(ModifierType type : ModifierType.values()){
		    config.put(type, type.getDefaultBlock());
		}gadget.saveDefaultConfig();
		log.info("[VoxelGadget] Default config created");
	    }
    }
}
