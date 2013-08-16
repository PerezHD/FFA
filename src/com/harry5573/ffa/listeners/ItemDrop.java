package com.harry5573.ffa.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.Plugin;

import com.harry5573.ffa.Main;

public class ItemDrop implements Listener {

    String prefix = ChatColor.GOLD + "[FFA]:";
    public static Main ms = null;

    public ItemDrop(Plugin plugin) {
        ms = (Main) plugin;
    }

    @EventHandler
    public void inventoryDrop(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        if (ms.killstreak.containsKey(p)) {
            event.setCancelled(true);
            p.sendMessage(prefix + ChatColor.RED + " You are not allowed to drop items in FFA.");
            p.updateInventory();
        }
    }
}