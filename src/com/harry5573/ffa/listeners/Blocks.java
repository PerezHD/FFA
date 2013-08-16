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

public class Blocks implements Listener {

    String prefix = ChatColor.GOLD + "[FFA]:";
    public static Main ms = null;

    public Blocks(Plugin plugin) {
        ms = (Main) plugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
            Player p = e.getPlayer();
            if (ms.killstreak.containsKey(p)) {
                e.setCancelled(true);
                p.sendMessage(prefix + ChatColor.GREEN + " You may not break blocks in ffa.");
            }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
            Player p = e.getPlayer();
            if (ms.killstreak.containsKey(p)) {
                e.setCancelled(true);
                p.sendMessage(prefix + ChatColor.GREEN + " You may not place blocks in ffa.");
            }
    }
}