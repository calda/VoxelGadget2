package com.thevoxelbox.gadget;

import static com.thevoxelbox.gadget.VoxelGadget.log;
import com.thevoxelbox.gadget.modifier.ComboBlock;
import com.thevoxelbox.gadget.modifier.ModifierType;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

public class GadgetListener implements Listener{
    
    final VoxelGadget gadget;
    final HashMap<ModifierType, ComboBlock> config = new HashMap<ModifierType, ComboBlock>();
    
    public GadgetListener(VoxelGadget gadget){
	this.gadget = gadget;
    }
    
    @EventHandler
    public void onDispenserDispense(BlockDispenseEvent e){
	
    }
    
    public void loadConfig() {
        try{
            File f = new File("plugins/VoxelGadget/config.txt");
            if (f.exists()){
                Scanner snr = new Scanner(f);
                snr.close();
                log.info("[VoxelGadget] Config loaded");
            }else gadget.saveConfig();
        }catch (FileNotFoundException e){
            log.warning("[VoxelGadget] Error while loading config.txt \t Loading default configuration.");
	    for(ModifierType type : ModifierType.values()){
		config.put(type, type.getDefaultBlock());
	    }
        }
    }
    
}
