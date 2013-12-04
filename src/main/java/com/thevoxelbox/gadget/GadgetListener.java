package com.thevoxelbox.gadget;

import static com.thevoxelbox.gadget.VoxelGadget.log;
import com.thevoxelbox.gadget.modifier.ComboBlock;
import com.thevoxelbox.gadget.modifier.ModifierType;
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
    
    public GadgetListener(VoxelGadget gadget){
	this.gadget = gadget;
    }
    
    @EventHandler
    public void onDispenserDispense(BlockDispenseEvent e){
	if(e.getItem().getType().isBlock()){
	    Processor processor = new Processor(config);
	    boolean success = processor.process(e);
	    e.setCancelled(success);
	}
    }
    
    public void loadConfig() {
        /*try{
            File f = new File("plugins/VoxelGadget/config.yml");
            if (f.exists()){
                Scanner snr = new Scanner(f);
                snr.close();
                log.info("[VoxelGadget] Config loaded");
            }else gadget.saveConfig();
        }catch (FileNotFoundException e){ */
            log.warning("[VoxelGadget] Error while loading config.yml \t Loading default configuration.");
	    for(ModifierType type : ModifierType.values()){
		config.put(type, type.getDefaultBlock());
	    }System.out.println(config);
        //}
    }
    
}
