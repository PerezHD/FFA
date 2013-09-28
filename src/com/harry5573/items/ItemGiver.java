/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.harry5573.items;

import com.harry5573.ffa.Main;
import com.harry5573.ffa.api.PlayerKillstreakEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 *
 * @author Harry5573
 */
public class ItemGiver implements Listener {
    
    public static Main plugin;
    
    public ItemGiver(Main instance) {
        this.plugin = instance;
    }
    
    @EventHandler(priority= EventPriority.HIGH)
    public void onItem(PlayerKillstreakEvent e) {
        Player p = e.getPlayer();
        int ks = e.getKillStreak();
        
        double streakVal = ks / 3.0;
        double bigstreakVal = ks / 10.0;
        
        plugin.ih.giveFood(p);
        
        if (streakVal >= 1) {
            plugin.giveMoney(p, plugin.getConfig().getDouble("3killcash"));
            Bukkit.broadcastMessage(plugin.getPrefix() + " " + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " is on a " + ChatColor.AQUA + ks + ChatColor.GRAY + " killstreak!");
        }
        
        if (bigstreakVal >= 1) {
            plugin.giveMoney(p, plugin.getConfig().getDouble("10killcash"));
            plugin.ih.giveArmour(p);
            Bukkit.broadcastMessage(plugin.getPrefix() + " " + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " is on a " + ChatColor.AQUA + ks + ChatColor.GRAY + " killstreak! Go stop him/her!");
        }
        if (ks == 1) {
            plugin.ih.addDiamondTools(p);
        }

        if (ks == 3) {
            plugin.ih.giveBowAndArrow(p);
        }

    }
}
