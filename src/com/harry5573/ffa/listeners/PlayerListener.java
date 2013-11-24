/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.harry5573.ffa.listeners;

import com.harry5573.ffa.FreeForAll;
import com.harry5573.ffa.api.PlayerKillPlayerInFFAEvent;
import com.harry5573.ffa.api.PlayersInFFAChangeEvent;
import com.harry5573.ffa.managers.MessageManager;
import com.harry5573.ffa.region.LocationTools;
import com.harry5573.ffa.region.Region;
import com.harry5573.ffa.region.Selection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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
import org.bukkit.util.Vector;

/**
 *
 * @author Harry5573
 */
public class PlayerListener implements Listener {

    FreeForAll plugin;

    public PlayerListener(FreeForAll instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (plugin.playerInFFA.contains(p)) {
            e.setCancelled(true);
            p.sendMessage(plugin.messageman.getPrefix() + ChatColor.GREEN + " You may not break blocks in ffa.");
        } 
    } 

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (plugin.playerInFFA.contains(p)) {
            e.setCancelled(true);
            p.sendMessage(plugin.messageman.getPrefix() + ChatColor.GREEN + " You may not place blocks in ffa.");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        if (plugin.playerInFFA.contains(p)) {
            p.sendMessage(plugin.messageman.getPrefix() + ChatColor.RED + " You can't use commands while in FFA!");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void inventoryFFADrop(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        if (plugin.playerInFFA.contains(p)) {
            event.setCancelled(true);
            p.sendMessage(plugin.messageman.getPrefix() + ChatColor.RED + " You are not allowed to drop items in FFA.");
            p.updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFFAOpenEvent(InventoryOpenEvent e) {
        Player p = (Player) e.getPlayer();
        if (!plugin.playerInFFA.contains(p)) {
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
        if (plugin.playerInFFA.contains(p)) {
            event.setCancelled(true);
            p.updateInventory();
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();

        if (plugin.warmupTasks.containsKey(p)) {
            if ((event.getFrom().getBlockX() != event.getTo().getBlockX()) || (event.getFrom().getBlockZ() != event.getTo().getBlockZ())) {
                Bukkit.getScheduler().cancelTask(plugin.warmupTasks.get(p));
                
                plugin.warmupTasks.remove(p);
                plugin.playerInFFA.remove(p);
                
                event.getPlayer().sendMessage(plugin.messageman.getPrefix() + ChatColor.RED + " Teleport cancelled due to player movement.");
            }
        }

        Region region = plugin.regionman.getFFARegion();
        Vector pt = LocationTools.toVector(event.getTo());
        Vector pf = LocationTools.toVector(event.getFrom());

        World world = p.getWorld();
        if (region.contains(world, pt) && !region.contains(world, pf)) {
            //If they enter
            p.sendMessage(plugin.messageman.getPrefix() + ChatColor.GOLD + " It seems you enterd the ffa region... Adding you to FFA");
            plugin.pmanager.joinFFA(p);
        } else if (!region.contains(world, pt) && region.contains(world, pf)) {
            //If they leave
            p.sendMessage(plugin.messageman.getPrefix() + ChatColor.GOLD + " It seems you left the ffa region... Removing you from FFA");
            plugin.pmanager.removeFromFFA(p, true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFFAQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if (plugin.playerInFFA.contains(p)) {
            plugin.pmanager.removeFromFFA(p, false);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onKick(PlayerKickEvent event) {
        Player p = event.getPlayer();
        if (plugin.playerInFFA.contains(p)) {
            plugin.pmanager.removeFromFFA(p, false);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void respawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        if (plugin.playerInFFA.contains(p)) {
            plugin.pmanager.removeFromFFA(p, true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (plugin.playerInFFA.contains(player)) {
            if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
                event.setCancelled(true);
                player.sendMessage(plugin.messageman.getPrefix() + ChatColor.GREEN + " You cant remove your armor in FFA");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
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
            p.sendMessage(plugin.messageman.getPrefix() + ChatColor.LIGHT_PURPLE + " Set Point 1: " + LocationTools.toVector(evtBlock));
            e.setCancelled(true);
        }
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Selection.setP1(LocationTools.toVector(evtBlock));
            p.sendMessage(plugin.messageman.getPrefix() + ChatColor.LIGHT_PURPLE + " Set Point 2: " + LocationTools.toVector(evtBlock));
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event) {
        final Player killed = event.getEntity().getPlayer();

        if (!plugin.playerInFFA.contains(killed)) {
            return;
        }

        event.getDrops().clear();
        event.setDroppedExp(0);
        event.setDeathMessage(null);

        Player killer = killed.getKiller();

        if (killer != null) {
            if (plugin.getConfig().getBoolean("effect.killsound")) {
                killer.playSound(killer.getLocation(), Sound.NOTE_BASS, 1F, 0);
            }

            int streak = plugin.playerKillstreak.get(killer);
            int newstreak = streak + 1;

            plugin.playerKillstreak.remove(killer);
            plugin.playerKillstreak.put(killer, newstreak);
            killer.setLevel(newstreak);

            PlayerKillPlayerInFFAEvent killstreakevent = new PlayerKillPlayerInFFAEvent(killer, newstreak);
            Bukkit.getServer().getPluginManager().callEvent(killstreakevent);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChange(PlayersInFFAChangeEvent e) {
        for (Player inFFA : plugin.playerInFFA) {
            plugin.pmanager.updateScoreboard(inFFA);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onItem(PlayerKillPlayerInFFAEvent e) {
        Player p = e.getPlayer();
        int ks = e.getKillStreak();

        for (Player inFFA : plugin.playerInFFA) {
            plugin.pmanager.updateScoreboard(inFFA);
        }

        plugin.rewardman.tryReward(p, ks);

        plugin.ih.giveFood(p);
    }
}
