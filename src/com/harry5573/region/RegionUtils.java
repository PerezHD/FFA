/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.harry5573.region;

import com.harry5573.ffa.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.util.BlockVector;

/**
 *
 * @author Harry5573
 */
public class RegionUtils {
    
    public static Main plugin;
    
    public RegionUtils(Main instance) {
        this.plugin = instance;
    }
    

    public Region getFFARegion() {
        World world = Bukkit.getWorld(plugin.getConfig().getString("region.world", "world"));
        double p1x = plugin.getConfig().getDouble("region.p1.x");
        double p1y = plugin.getConfig().getDouble("region.p1.y");
        double p1z = plugin.getConfig().getDouble("region.p1.z");
        double p2x = plugin.getConfig().getDouble("region.p2.x");
        double p2y = plugin.getConfig().getDouble("region.p2.y");
        double p2z = plugin.getConfig().getDouble("region.p2.z");
        BlockVector p1 = new BlockVector(p1x, p1y, p1z);
        BlockVector p2 = new BlockVector(p2x, p2y, p2z);
        return new Region(world, p1, p2);
    }
}
