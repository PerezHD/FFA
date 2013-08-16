package com.harry5573.ffa.killstreaks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.harry5573.ffa.Main;

public class Killstreak implements Listener {

    String prefix = ChatColor.GOLD + "[FFA]:";
    String prefix2 = ChatColor.YELLOW + "[" + ChatColor.RED + "FFA" + ChatColor.YELLOW + "]";
    public static Main ms = null;

    public Killstreak(Plugin plugin) {
        ms = (Main) plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event) {
        //WEAPONS
        ItemStack sworddiamond = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemStack axediamond = new ItemStack(Material.DIAMOND_AXE, 1);
        sworddiamond.addEnchantment(Enchantment.DURABILITY, 3);
        sworddiamond.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        axediamond.addEnchantment(Enchantment.DURABILITY, 3);
        axediamond.addEnchantment(Enchantment.DAMAGE_ALL, 1);

        ItemMeta meta1 = axediamond.getItemMeta();
        List loreData1 = new ArrayList();
        loreData1.add(ChatColor.BLUE + "FFA Item");
        meta1.setDisplayName(ChatColor.GREEN + "FFA Axe");
        meta1.setLore(loreData1);
        axediamond.setItemMeta(meta1);

        ItemMeta meta7 = sworddiamond.getItemMeta();
        List loreData7 = new ArrayList();
        loreData7.add(ChatColor.BLUE + "FFA Item");
        meta7.setDisplayName(ChatColor.GREEN + "FFA Sword");
        meta7.setLore(loreData7);
        sworddiamond.setItemMeta(meta7);
        //New Bow For Player After 3 Kills
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        ItemStack arrow = new ItemStack(Material.ARROW, 1);
        //New Armour
        ItemStack helmet = new ItemStack(Material.IRON_HELMET, 1);
        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
        helmet.addEnchantment(Enchantment.DURABILITY, 3);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        chestplate.addEnchantment(Enchantment.DURABILITY, 3);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        leggings.addEnchantment(Enchantment.DURABILITY, 3);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        boots.addEnchantment(Enchantment.DURABILITY, 3);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        //Name Armour
        ItemMeta meta2 = helmet.getItemMeta();
        List loreData2 = new ArrayList();
        loreData2.add(ChatColor.BLUE + "FFA Item");
        meta2.setDisplayName(ChatColor.GREEN + "FFA Helmet");
        meta2.setLore(loreData2);
        helmet.setItemMeta(meta2);

        ItemMeta meta3 = chestplate.getItemMeta();
        List loreData3 = new ArrayList();
        loreData3.add(ChatColor.BLUE + "FFA Item");
        meta3.setDisplayName(ChatColor.GREEN + "FFA Chestplate");
        meta3.setLore(loreData3);
        chestplate.setItemMeta(meta3);

        ItemMeta meta4 = leggings.getItemMeta();
        List loreData4 = new ArrayList();
        loreData4.add(ChatColor.BLUE + "FFA Item");
        meta4.setDisplayName(ChatColor.GREEN + "FFA Leggings");
        meta4.setLore(loreData4);
        leggings.setItemMeta(meta4);

        ItemMeta meta5 = boots.getItemMeta();
        List loreData5 = new ArrayList();
        loreData5.add(ChatColor.BLUE + "FFA Item");
        meta5.setDisplayName(ChatColor.GREEN + "FFA Boots");
        meta5.setLore(loreData5);
        boots.setItemMeta(meta5);

        Entity e = event.getEntity();
        if ((e instanceof Player) && (ms.killstreak.containsKey((Player) e))) {
            Player killed = event.getEntity();
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tptoggle " + killed.getName() + " on");
            event.getDrops().removeAll(event.getDrops());
            Player killer = ((Player) e).getKiller();
            if (killer != null) {
                int x = ((Integer) ms.killstreak.get(killer)).intValue();
                event.setDroppedExp(0);
                event.setDeathMessage("");
                int newx = x + 1;
                ms.killstreak.remove(killer);
                ms.killstreak.put(killer, Integer.valueOf(newx));
                killer.giveExpLevels(1);
                //Give new potions :)
                ms.givePotions(killer);
                if (newx == 1) {
                    killer.getInventory().setItem(0, sworddiamond);
                    killer.getInventory().setItem(1, axediamond);
                    killer.sendMessage(prefix + ChatColor.GRAY + " Your Weapons Have Been Upgraded!");
                } else if (newx == 3) {
                    killer.getInventory().setItem(6, bow);
                    killer.getInventory().setItem(7, arrow);
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 500");
                    killer.playSound(killer.getLocation(), Sound.GHAST_SCREAM, 5.0F, 1.0F);
                    Bukkit.broadcastMessage(prefix2 + ChatColor.DARK_RED + " " + killer.getName() + ChatColor.GRAY + " Is on a" + ChatColor.AQUA + " 3 " + ChatColor.GRAY + "killstreak and has recived " + ChatColor.LIGHT_PURPLE + "$500");
                } else if (newx == 6) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 500");
                    killer.playSound(killer.getLocation(), Sound.GHAST_SCREAM, 5.0F, 1.0F);
                    Bukkit.broadcastMessage(prefix2 + ChatColor.DARK_RED + " " + killer.getName() + ChatColor.GRAY + " Is on a" + ChatColor.AQUA + " 6 " + ChatColor.GRAY + "killstreak and has recived " + ChatColor.LIGHT_PURPLE + "$500");
                } else if (newx == 9) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 500");
                    killer.playSound(killer.getLocation(), Sound.GHAST_SCREAM, 5.0F, 1.0F);
                    Bukkit.broadcastMessage(prefix2 + ChatColor.DARK_RED + " " + killer.getName() + ChatColor.GRAY + " Is on a" + ChatColor.AQUA + " 9 " + ChatColor.GRAY + "killstreak and has recived " + ChatColor.LIGHT_PURPLE + "$500");
                } else if (newx == 12) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 2500");
                    killer.playSound(killer.getLocation(), Sound.GHAST_SCREAM, 5.0F, 1.0F);
                    Bukkit.broadcastMessage(prefix2 + ChatColor.DARK_RED + " " + killer.getName() + ChatColor.GRAY + " Is on a" + ChatColor.AQUA + " 12 " + ChatColor.GRAY + "killstreak and has recived " + ChatColor.LIGHT_PURPLE + "$2500 " + ChatColor.GRAY + " + " + ChatColor.LIGHT_PURPLE + "New Armour" + ChatColor.GRAY + " + " + ChatColor.LIGHT_PURPLE + "Speed 3");
                    killer.getInventory().setHelmet(helmet);
                    killer.getInventory().setChestplate(chestplate);
                    killer.getInventory().setLeggings(leggings);
                    killer.getInventory().setBoots(boots);
                    killer.sendMessage(prefix + ChatColor.YELLOW + " You have hit a 12 killstreak and have received new armor and speed 3");
                    killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3000000, 2));
                } else if (newx == 15) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 500");
                    killer.playSound(killer.getLocation(), Sound.GHAST_SCREAM, 5.0F, 1.0F);
                    Bukkit.broadcastMessage(prefix2 + ChatColor.DARK_RED + " " + killer.getName() + ChatColor.GRAY + " Is on a" + ChatColor.AQUA + " 15 " + ChatColor.GRAY + "killstreak and has recived " + ChatColor.LIGHT_PURPLE + "$500");
                } else if (newx == 18) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 500");
                    killer.playSound(killer.getLocation(), Sound.GHAST_SCREAM, 5.0F, 1.0F);
                    Bukkit.broadcastMessage(prefix2 + ChatColor.DARK_RED + " " + killer.getName() + ChatColor.GRAY + " Is on a" + ChatColor.AQUA + " 18 " + ChatColor.GRAY + "killstreak and has recived " + ChatColor.LIGHT_PURPLE + "$500");
                } else if (newx == 21) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 500");
                    killer.playSound(killer.getLocation(), Sound.GHAST_SCREAM, 5.0F, 1.0F);
                    Bukkit.broadcastMessage(prefix2 + ChatColor.DARK_RED + " " + killer.getName() + ChatColor.GRAY + " Is on a" + ChatColor.AQUA + " 21 " + ChatColor.GRAY + "killstreak and has recived " + ChatColor.LIGHT_PURPLE + "$500");
                } else if (newx == 24) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 5000");
                    killer.playSound(killer.getLocation(), Sound.GHAST_SCREAM, 5.0F, 1.0F);
                    Bukkit.broadcastMessage(prefix2 + ChatColor.DARK_RED + " " + killer.getName() + ChatColor.GRAY + " Is on a" + ChatColor.AQUA + " 24 " + ChatColor.GRAY + "killstreak and has recived " + ChatColor.LIGHT_PURPLE + "$5000 " + ChatColor.GRAY + " + " + ChatColor.LIGHT_PURPLE + "New Armour" + ChatColor.GRAY + " + " + ChatColor.LIGHT_PURPLE + "Strength");
                    killer.getInventory().setHelmet(helmet);
                    killer.getInventory().setChestplate(chestplate);
                    killer.getInventory().setLeggings(leggings);
                    killer.getInventory().setBoots(boots);
                    killer.sendMessage(prefix + ChatColor.YELLOW + " You have hit a 24 killstreak and have received new armour and strength");
                    killer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3000000, 0));
                } else if (newx == 27) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 500");
                    killer.playSound(killer.getLocation(), Sound.GHAST_SCREAM, 5.0F, 1.0F);
                    Bukkit.broadcastMessage(prefix2 + ChatColor.DARK_RED + " " + killer.getName() + ChatColor.GRAY + " Is on a" + ChatColor.AQUA + " 27 " + ChatColor.GRAY + "killstreak and has recived " + ChatColor.LIGHT_PURPLE + "$500");
                } else if (newx == 30) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 500");
                    killer.playSound(killer.getLocation(), Sound.GHAST_SCREAM, 5.0F, 1.0F);
                    Bukkit.broadcastMessage(prefix2 + ChatColor.DARK_RED + " " + killer.getName() + ChatColor.GRAY + " Is on a" + ChatColor.AQUA + " 30 " + ChatColor.GRAY + "killstreak and has recived " + ChatColor.LIGHT_PURPLE + "$500");
                } else if (newx == 33) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 500");
                    killer.playSound(killer.getLocation(), Sound.GHAST_SCREAM, 5.0F, 1.0F);
                    Bukkit.broadcastMessage(prefix2 + ChatColor.DARK_RED + " " + killer.getName() + ChatColor.GRAY + " Is on a" + ChatColor.AQUA + " 33 " + ChatColor.GRAY + "killstreak and has recived " + ChatColor.LIGHT_PURPLE + "$500");
                } else if (newx == 36) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 7500");
                    killer.playSound(killer.getLocation(), Sound.GHAST_SCREAM, 5.0F, 1.0F);
                    Bukkit.broadcastMessage(prefix2 + ChatColor.DARK_RED + " " + killer.getName() + ChatColor.GRAY + " Is on a" + ChatColor.AQUA + " 36 " + ChatColor.GRAY + "killstreak and has recived " + ChatColor.LIGHT_PURPLE + "$7500     " + ChatColor.GRAY + " + " + ChatColor.LIGHT_PURPLE + "New Armour" + ChatColor.GRAY + " + " + ChatColor.DARK_RED + "Regeneration");
                    killer.getInventory().setHelmet(helmet);
                    killer.getInventory().setChestplate(chestplate);
                    killer.getInventory().setLeggings(leggings);
                    killer.getInventory().setBoots(boots);
                    killer.sendMessage(prefix + ChatColor.YELLOW + " You have hit a 36 killstreak and have received new armour and regeration");
                    killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 3000000, 0));
                }
            }
        }
    }
}
