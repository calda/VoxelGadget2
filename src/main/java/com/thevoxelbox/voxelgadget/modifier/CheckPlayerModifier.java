/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 *
 * @author Piotr Przerwa <przerwap@gmail.com>
 */
class CheckPlayerModifier extends AbstractCheckModifier {

    @Override
    public int modify(Processor p) {
        Block existing = p.getDispenser().getRelative(p.getTrain().getOppositeFace(), p.getOffset());
        if (p.getOffset3D() != null) {
            existing = p.getOffset3D().getBlock();
        }
        if (p.isAreaEnabled()) {
            Vector v = new Vector(existing.getX(), existing.getY(), existing.getZ());
            double r = p.getOffset();
            for (Player pl : p.getDispenser().getWorld().getPlayers()) {
                if (pl.getLocation().toVector().isInSphere(v, r)) {
                    p.setCheck(true);
                    return 0;
                }
            }
        } else {
            int x = existing.getX();
            int y = existing.getY();
            int z = existing.getZ();
            for (Player pl : p.getDispenser().getWorld().getPlayers()) {
                Location ploc = pl.getLocation();
                if (ploc.getBlockX() == x && ploc.getBlockY() == y && ploc.getBlockZ() == z) {
                    p.setCheck(true);
                    return 0;
                }
            }
        }
        p.setCheck(false);
        return 0;
    }

    @Override
    public boolean runCheck(Inventory target, Inventory dispenser, ItemStack dispensed) {
        return false;
    }
}
