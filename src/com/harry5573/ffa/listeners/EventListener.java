/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.harry5573.ffa.listeners;

import com.harry5573.ffa.Main;
import com.harry5573.region.LocationTools;
import com.harry5573.region.Region;
import com.harry5573.region.Selection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

/**
 *
 * @author Harry5573
 */
public class EventListener implements Listener {

    public static Main plugin;

    public EventListener(Main instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (plugin.killstreak.containsKey(p)) {
            e.setCancelled(true);
            p.sendMessage(plugin.getPrefix() + ChatColor.GREEN + " You may not break blocks in ffa.");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (plugin.killstreak.containsKey(p)) {
            e.setCancelled(true);
            p.sendMessage(plugin.getPrefix() + ChatColor.GREEN + " You may not place blocks in ffa.");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        
        if (plugin.killstreak.containsKey(p) || (plugin.timer.containsKey(p))) {
            p.sendMessage(plugin.getPrefix() + ChatColor.RED + " You can't use commands while in FFA!");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void inventoryFFADrop(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        if (plugin.killstreak.containsKey(p)) {
            event.setCancelled(true);
            p.sendMessage(plugin.getPrefix() + ChatColor.RED + " You are not allowed to drop items in FFA.");
            p.updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFFAPickupItem(PlayerPickupItemEvent event) {
        Player p = event.getPlayer();
        if (plugin.killstreak.containsKey(p)) {
            event.setCancelled(true);
            p.updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();

        if (plugin.timer.containsKey(p)) {
            if ((event.getFrom().getBlockX() != event.getTo().getBlockX()) || (event.getFrom().getBlockZ() != event.getTo().getBlockZ())) {
                Bukkit.getScheduler().cancelTask(((Integer) plugin.timer.get(p)).intValue());
                plugin.timer.remove(p);
                event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + " Teleport cancelled due to player movement.");
            }
        }

        Region region = plugin.getFFARegion();
        Vector pt = LocationTools.toVector(event.getTo());
        Vector pf = LocationTools.toVector(event.getFrom());
        World world = p.getWorld();
        if (region.contains(world, pt) && !region.contains(world, pf)) {
            //If they enter
            p.sendMessage(plugin.getPrefix() + ChatColor.GOLD + " It seems you enterd the ffa region... Adding you to FFA");
            plugin.joinFFA(p);
        } else if (!region.contains(world, pt) && region.contains(world, pf)) {
            //If they leave
            p.sendMessage(plugin.getPrefix() + ChatColor.GOLD + " It seems you left the ffa region... Removing you from FFA");
            plugin.removeFromFFA(p);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFFAQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if ((plugin.killstreak.containsKey(p))) {
            plugin.removeFromFFA(p);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onKick(PlayerKickEvent event) {
        Player p = event.getPlayer();
        if ((plugin.killstreak.containsKey(p))) {
            plugin.removeFromFFA(p);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void respawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        if ((plugin.killstreak.containsKey(p))) {
            plugin.respawnFFA(p);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (plugin.killstreak.containsKey(player)) {
            if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
                event.setCancelled(true);
                player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + " You cant remove your armour in FFA");
            }
            if (event.getSlot() == 0) {
                event.setCancelled(true);
                player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + " You cant move your sword/axe in FFA");
            }
            if (event.getSlot() == 1) {
                event.setCancelled(true);
                player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + " You cant move your sword/axe in FFA");
            }
        }
    }

    //Stop them from teleporting
    @EventHandler(priority = EventPriority.LOWEST)
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        {
            if ((event.getCause().equals(PlayerTeleportEvent.TeleportCause.COMMAND)) || (event.getCause().equals(PlayerTeleportEvent.TeleportCause.PLUGIN))) {
                if (plugin.killstreak.containsKey(player)) {
                    event.setCancelled(true);
                    player.sendMessage(plugin.getPrefix() + ChatColor.LIGHT_PURPLE + " You cannot teleport in ffa");
                }
            }
        }
    }
    
    @EventHandler(priority= EventPriority.LOWEST)
    public void onPlayerFFAInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        
        if (p.getItemInHand().getType() != Material.WOOD_SWORD) {
            return;
        }

        if (!p.isOp()) {
            return;
        }

        Block evtBlock = e.getClickedBlock();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Selection.setP2(LocationTools.toVector(evtBlock));
            p.sendMessage(plugin.getPrefix() + ChatColor.LIGHT_PURPLE + " Set Point 1: " + LocationTools.toVector(evtBlock));
            e.setCancelled(true);
        }
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Selection.setP1(LocationTools.toVector(evtBlock));
            p.sendMessage(plugin.getPrefix() + ChatColor.LIGHT_PURPLE + " Set Point 2: " + LocationTools.toVector(evtBlock));
            e.setCancelled(true);
        }
    }
}
