package com.thevoxelbox.voxelgadget;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class VoxelGadget extends JavaPlugin {

    protected static final Logger log = Logger.getLogger("Minecraft");
    final GadgetListener listener = new GadgetListener(this);

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(listener, this);
        listener.loadConfig();
    }

}
