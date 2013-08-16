package com.harry5573.ffa.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.Plugin;

import com.harry5573.ffa.Main;

public class ItemPickup implements Listener {

    String prefix = ChatColor.GOLD + "[FFA]:";
    public static Main ms = null;

    public ItemPickup(Plugin plugin) {
        ms = (Main) plugin;
    }

    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        Player p = event.getPlayer();
        if (ms.killstreak.containsKey(p)) {
            event.setCancelled(true);
        }
        p.updateInventory();
    }
}