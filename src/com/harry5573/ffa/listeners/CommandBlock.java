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

    String prefix = ChatColor.GOLD + "[FFA]:";
    public static Main ms = null;

    public CommandBlock(Plugin plugin) {
        ms = (Main) plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        if (ms.killstreak.containsKey(p)) {
            p.sendMessage(prefix + ChatColor.RED + " You can't use commands while in FFA!");
            event.setCancelled(true);
        }
    }
    
    
    
}