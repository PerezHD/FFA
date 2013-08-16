package com.harry5573.ffa.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import com.harry5573.ffa.Main;

public class CommandBlock implements Listener {

    Main plugin;

    public CommandBlock(Main instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        if (plugin.killstreak.containsKey(p)) {
            p.sendMessage(plugin.prefix + ChatColor.RED + " You can't use commands while in FFA!");
            event.setCancelled(true);
        }
    }
    
    
    
}