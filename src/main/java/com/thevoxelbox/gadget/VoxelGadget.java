package com.thevoxelbox.gadget;

import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class VoxelGadget extends JavaPlugin{
    
    protected static final Logger log = Logger.getLogger("Minecraft");
    final GadgetListener listener = new GadgetListener(this);
    
    @Override
    public void onDisable(){
    }

    @Override
    public void onEnable(){
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
    }
}