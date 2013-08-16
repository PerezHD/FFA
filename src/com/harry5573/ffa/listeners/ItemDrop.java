package com.harry5573.ffa.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.Plugin;

import com.harry5573.ffa.Main;

public class ItemDrop implements Listener {

    Main plugin;

    public ItemDrop(Main instance) {
        this.plugin = instance;
    }

    @EventHandler
    public void inventoryDrop(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        if (plugin.killstreak.containsKey(p)) {
            event.setCancelled(true);
            p.sendMessage(plugin.prefix + ChatColor.RED + " You are not allowed to drop items in FFA.");
            p.updateInventory();
        }
    }
}