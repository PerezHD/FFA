/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.harry5573.ffa.listeners;

import com.harry5573.ffa.Main;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

/**
 *
 * @author Harry5573
 */
public class ItemHandler {

    public static Main plugin;

    public ItemHandler(Main instance) {
        this.plugin = instance;
    }

    public void giveItems(Player p) {
        //Give the player a fresh set of health and hunger
        p.setFoodLevel(20);
        p.setHealth(20);

        //Close And Clear The Players Old Inventory
        p.getOpenInventory().close();
        p.getInventory().clear();

        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);

        p.updateInventory();

        //Remove all old player potion effects
        for (PotionEffect pf : p.getActivePotionEffects()) {
            p.removePotionEffect(pf.getType());
        }
        
        addIronTools(p);

        giveArmour(p);
    }

    public void givePotions(Player p) {
        ItemStack regen = new ItemStack(Material.POTION, 1, (short) 16481);
        ItemStack health = new ItemStack(Material.POTION, 1, (short) 16421);

        ItemStack food = new ItemStack(Material.COOKED_BEEF, 32);

        p.getInventory().addItem(regen);
        p.getInventory().addItem(regen);
        p.getInventory().addItem(regen);
        p.getInventory().addItem(regen);
        p.getInventory().addItem(regen);

        p.getInventory().setItem(9, food);


        p.getInventory().addItem(health);
        p.getInventory().addItem(health);
        p.getInventory().addItem(health);
        p.getInventory().addItem(health);
        p.getInventory().addItem(health);
        p.getInventory().addItem(health);
        p.getInventory().addItem(health);
        p.getInventory().addItem(health);
        p.getInventory().addItem(health);
        p.getInventory().addItem(health);
        p.getInventory().addItem(health);
        p.getInventory().addItem(health);
        p.getInventory().addItem(health);
        p.getInventory().addItem(health);
        p.getInventory().addItem(health);
        p.getInventory().addItem(health);
        p.getInventory().addItem(health);
        p.getInventory().addItem(health);
        p.getInventory().addItem(health);
        p.getInventory().addItem(health);
    }
    
    public void addIronTools(Player p) {
                //Weapons (Sword and Axe)
        ItemStack axe = new ItemStack(Material.IRON_AXE);
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        //Enchant The Sword And Axe
        axe.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        axe.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
        sword.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
        
        ItemMeta meta = sword.getItemMeta();
        List<String> loreData = new ArrayList();
        loreData.add(ChatColor.BLUE + "FFA Item");
        meta.setDisplayName(ChatColor.GREEN + "FFA Sword");
        meta.setLore(loreData);
        sword.setItemMeta(meta);

        ItemMeta meta1 = axe.getItemMeta();
        List loreData1 = new ArrayList();
        loreData1.add(ChatColor.BLUE + "FFA Item");
        meta1.setDisplayName(ChatColor.GREEN + "FFA Axe");
        meta1.setLore(loreData1);
        axe.setItemMeta(meta1);

        //Give the players the weapons + food
        p.getInventory().setItem(0, sword);
        p.getInventory().setItem(1, axe);
    }
    
    public void giveBow(Player p) {
        //New Bow For Player After 3 Kills
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        ItemStack arrow = new ItemStack(Material.ARROW, 1);

        p.getInventory().setItem(8, bow);
        p.getInventory().setItem(9, arrow);
    }

    public void addDiamondTools(Player p) {
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

        p.getInventory().setItem(0, sworddiamond);
        p.getInventory().setItem(1, axediamond);
    }

    public void giveArmour(Player p) {

        //Armour
        ItemStack helmet = new ItemStack(Material.IRON_HELMET, 1);
        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
        
        //Enchant the armour
        helmet.addEnchantment(Enchantment.DURABILITY, 3);
        chestplate.addEnchantment(Enchantment.DURABILITY, 3);
        leggings.addEnchantment(Enchantment.DURABILITY, 3);
        boots.addEnchantment(Enchantment.DURABILITY, 3);
        
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

        p.getInventory().setHelmet(helmet);
        p.getInventory().setChestplate(chestplate);
        p.getInventory().setLeggings(leggings);
        p.getInventory().setBoots(boots);
    }
}
