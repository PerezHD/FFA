package com.harry5573.ffa.killstreaks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.harry5573.ffa.Main;

public class Killstreak implements Listener {
    
    Main plugin;

    public Killstreak(Main instance) {
        this.plugin = instance;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        Entity e = event.getEntity();
        if ((e instanceof Player) && (plugin.killstreak.containsKey((Player) e))) {
            Player killed = event.getEntity();

            event.getDrops().clear();
            
            Player killer = ((Player) e).getKiller();
            
            if (killer != null) {
                
                
                int x = ((Integer) plugin.killstreak.get(killer)).intValue();
                event.setDroppedExp(0);
                event.setDeathMessage(null);
                int newx = x + 1;
                
                plugin.killstreak.remove(killer);
                plugin.killstreak.put(killer, Integer.valueOf(newx));
                killer.giveExpLevels(1);
                
                //Give new potions :)
                plugin.ih.giveStuff(killer);
                if (newx == 1) {
                    plugin.ih.addDiamondTools(killer);
                    killer.sendMessage(plugin.prefix + ChatColor.GRAY + " Your Weapons Have Been Upgraded!");
                } else if ((newx == 3) || (newx == 6) || (newx == 9) || (newx == 15) || (newx == 18) || (newx == 21) || (newx == 27) || (newx == 30) || (newx == 33) || (newx == 39) || (newx == 42) || (newx == 45) |(newx == 51) || (newx == 54)) {
                    plugin.ih.giveBow(killer);
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 10000");
                    killer.playSound(killer.getLocation(), Sound.GHAST_SCREAM, 5.0F, 1.0F);
                    Bukkit.broadcastMessage(plugin.prefix + ChatColor.DARK_RED + " " + killer.getName() + ChatColor.GRAY + " Is on a " + ChatColor.AQUA + newx + ChatColor.GRAY + " killstreak and has recived " + ChatColor.LIGHT_PURPLE + "$10000");
                } else if (newx == 12) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 50000");
                    killer.playSound(killer.getLocation(), Sound.GHAST_SCREAM, 5.0F, 1.0F);    
                    plugin.ih.giveArmour(killer);
                    killer.sendMessage(plugin.prefix + ChatColor.YELLOW + " You have hit a 12 killstreak and have received new armor and speed 3");
                    killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
                } else if (newx == 24) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 50000");
                    killer.playSound(killer.getLocation(), Sound.GHAST_SCREAM, 5.0F, 1.0F);
                    Bukkit.broadcastMessage(plugin.prefix + ChatColor.DARK_RED + " " + killer.getName() + ChatColor.GRAY + " Is on a" + ChatColor.AQUA + " 24 " + ChatColor.GRAY + "killstreak and has recived " + ChatColor.LIGHT_PURPLE + "$50000 " + ChatColor.GRAY + " + " + ChatColor.LIGHT_PURPLE + "New Armour" + ChatColor.GRAY + " + " + ChatColor.LIGHT_PURPLE + "Strength");
                    plugin.ih.giveArmour(killer);
                    killer.sendMessage(plugin.prefix + ChatColor.YELLOW + " You have hit a 24 killstreak and have received new armour and strength");
                    killer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));
                } else if (newx == 36) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 50000");
                    killer.playSound(killer.getLocation(), Sound.GHAST_SCREAM, 5.0F, 1.0F);
                    Bukkit.broadcastMessage(plugin.prefix + ChatColor.DARK_RED + " " + killer.getName() + ChatColor.GRAY + " Is on a" + ChatColor.AQUA + " 36 " + ChatColor.GRAY + "killstreak and has recived " + ChatColor.LIGHT_PURPLE + "$50000     " + ChatColor.GRAY + " + " + ChatColor.LIGHT_PURPLE + "New Armour" + ChatColor.GRAY + " + " + ChatColor.DARK_RED + "Regeneration");
                    plugin.ih.giveArmour(killer);
                    killer.sendMessage(plugin.prefix + ChatColor.YELLOW + " You have hit a 36 killstreak and have received new armour and regeration");
                    killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
                } else if (newx == 48) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 50000");
                    killer.playSound(killer.getLocation(), Sound.GHAST_SCREAM, 5.0F, 1.0F);
                    Bukkit.broadcastMessage(plugin.prefix + ChatColor.DARK_RED + " " + killer.getName() + ChatColor.GRAY + " Is on a" + ChatColor.AQUA + " 36 " + ChatColor.GRAY + "killstreak and has recived " + ChatColor.LIGHT_PURPLE + "$50000     " + ChatColor.GRAY + " + " + ChatColor.LIGHT_PURPLE + "New Armour" + ChatColor.GRAY + " + " + ChatColor.DARK_RED + "Regeneration");
                    plugin.ih.giveArmour(killer);
                    killer.sendMessage(plugin.prefix + ChatColor.YELLOW + " You have hit a 48 killstreak and have received new armour");
                }
            }
        }
    }
}
