package com.harry5573.ffa.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import com.harry5573.ffa.Main;
import org.bukkit.event.EventPriority;

public class Blocks implements Listener {

    Main plugin;

    public Blocks(Main instance) {
        this.plugin = instance;
    }

    @EventHandler(priority= EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent e) {
            Player p = e.getPlayer();
            if (plugin.killstreak.containsKey(p)) {
                e.setCancelled(true);
                p.sendMessage(plugin.prefix + ChatColor.GREEN + " You may not break blocks in ffa.");
            }
    }

    @EventHandler(priority= EventPriority.LOWEST)
    public void onPlace(BlockPlaceEvent e) {
            Player p = e.getPlayer();
            if (plugin.killstreak.containsKey(p)) {
                e.setCancelled(true);
                p.sendMessage(plugin.prefix + ChatColor.GREEN + " You may not place blocks in ffa.");
            }
    }
}