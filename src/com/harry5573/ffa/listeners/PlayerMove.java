package com.harry5573.ffa.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import com.harry5573.ffa.Main;

public class PlayerMove implements Listener {

    String prefix = ChatColor.GOLD + "[FFA]:";
    public static Main ms = null;

    public PlayerMove(Plugin plugin) {
        ms = (Main) plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();

        if (ms.timer.containsKey(p)) {
            if ((event.getFrom().getBlockX() != event.getTo().getBlockX())
                    || (event.getFrom().getBlockZ() != event.getTo().getBlockZ())) {
                Bukkit.getScheduler().cancelTask(((Integer) ms.timer.get(p)).intValue());
                ms.timer.remove(p);
                event.getPlayer().sendMessage(prefix + ChatColor.RED + " Teleport cancelled due to player movement.");
            }
        }
    }
}
