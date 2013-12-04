package com.thevoxelbox.gadget;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
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
	Bukkit.getServer().getPluginManager().registerEvents(listener, this);
	listener.loadConfig();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cc, String label, String[] args){
	if(sender instanceof Player == false){
	    sender.sendMessage("You must run this command from in-game");
	    return true;
	}
	if(label.equalsIgnoreCase("gadget")){
	    if(args.length >= 1){
		if(args[0].equalsIgnoreCase("point")){
		    
		}else if(args[0].equalsIgnoreCase("save")){
		    
		}
	    }
	}
	return true;
    }
    
}