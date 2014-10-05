/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.harry5573.ffa.managers;

import com.harry5573.ffa.FreeForAll;
import com.harry5573.ffa.region.Region;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.util.BlockVector;

/**
 *
 * @author Harry5573
 */
public class RegionManager {

      FreeForAll plugin;

      public RegionManager(FreeForAll instance) {
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

      public void setRegion(World world, BlockVector p1, BlockVector p2) {
            plugin.getConfig().set("region.world", world.getName());
            plugin.getConfig().set("region.p1.x", p1.getX());
            plugin.getConfig().set("region.p1.y", p1.getY());
            plugin.getConfig().set("region.p1.z", p1.getZ());
            plugin.getConfig().set("region.p2.x", p2.getX());
            plugin.getConfig().set("region.p2.y", p2.getY());
            plugin.getConfig().set("region.p2.z", p2.getZ());
            plugin.saveConfig();
      }
}
