/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.harry5573.ffa.listeners;

import com.harry5573.ffa.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author Harry5573
 */
public class EventListener implements Listener {

    public static Main plugin;

    public EventListener(Main instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (plugin.killstreak.containsKey(p)) {
            e.setCancelled(true);
            p.sendMessage(plugin.prefix + ChatColor.GREEN + " You may not break blocks in ffa.");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (plugin.killstreak.containsKey(p)) {
            e.setCancelled(true);
            p.sendMessage(plugin.prefix + ChatColor.GREEN + " You may not place blocks in ffa.");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        if (plugin.killstreak.containsKey(p)) {
            p.sendMessage(plugin.prefix + ChatColor.RED + " You can't use commands while in FFA!");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void inventoryFFADrop(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        if (plugin.killstreak.containsKey(p)) {
            event.setCancelled(true);
            p.sendMessage(plugin.prefix + ChatColor.RED + " You are not allowed to drop items in FFA.");
            p.updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFFAOpenEvent(InventoryOpenEvent e) {
        Player p = (Player) e.getPlayer();
        if (!(plugin.killstreak.containsKey(p))) {
            return;
        }
        if (e.getInventory().getType() == InventoryType.CHEST) {
            e.setCancelled(true);
        }
        if (e.getInventory().getType() == InventoryType.WORKBENCH) {
            e.setCancelled(true);
        }
        if (e.getInventory().getType() == InventoryType.ANVIL) {
            e.setCancelled(true);
        }
        if (e.getInventory().getType() == InventoryType.BREWING) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFFAPickupItem(PlayerPickupItemEvent event) {
        Player p = event.getPlayer();
        if (plugin.killstreak.containsKey(p)) {
            event.setCancelled(true);
            p.updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();

        if (plugin.timer.containsKey(p)) {
            if ((event.getFrom().getBlockX() != event.getTo().getBlockX())
                    || (event.getFrom().getBlockZ() != event.getTo().getBlockZ())) {
                Bukkit.getScheduler().cancelTask(((Integer) plugin.timer.get(p)).intValue());
                plugin.timer.remove(p);
                event.getPlayer().sendMessage(plugin.prefix + ChatColor.RED + " Teleport cancelled due to player movement.");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFFAQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if ((plugin.killstreak.containsKey(p))) {
            plugin.removeFromFFA(p);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKick(PlayerKickEvent event) {
        Player p = event.getPlayer();
        if ((plugin.killstreak.containsKey(p))) {
            plugin.removeFromFFA(p);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void respawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        if ((plugin.killstreak.containsKey(p))) {
            plugin.respawnFFA(p);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (plugin.killstreak.containsKey(player)) {
            if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
                event.setCancelled(true);
                player.sendMessage(plugin.prefix + ChatColor.GREEN + " You cant remove your armour in FFA");
            }
            if (event.getSlot() == 0) {
                event.setCancelled(true);
                player.sendMessage(plugin.prefix + ChatColor.GREEN + " You cant move your sword/axe in FFA");
            }
            if (event.getSlot() == 1) {
                event.setCancelled(true);
                player.sendMessage(plugin.prefix + ChatColor.GREEN + " You cant move your sword/axe in FFA");
            }
        }
    }

    //Stop them from teleporting
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        {
            if ((event.getCause().equals(PlayerTeleportEvent.TeleportCause.COMMAND)) || (event.getCause().equals(PlayerTeleportEvent.TeleportCause.PLUGIN))) {
                if (plugin.killstreak.containsKey(player)) {
                    event.setCancelled(true);
                    player.sendMessage(plugin.prefix + ChatColor.LIGHT_PURPLE + " You cannot teleport in ffa");
                }
            }
        }
    }
}
