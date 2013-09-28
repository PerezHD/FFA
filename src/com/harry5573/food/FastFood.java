/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.harry5573.food;

import com.harry5573.ffa.Main;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Harry5573
 */
public class FastFood implements Listener {

    public static Main plugin;

    public FastFood(Main instance) {
        this.plugin = instance;
    }

    @EventHandler(priority= EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        
        if (event.getAction() == null) {
            return;
        }               
        
        Action a = event.getAction();
        
        if (!event.hasItem() && !event.hasBlock()) {
            return;
        }

        if (event.getItem() == null) {
            return;
        }
        
        if (event.getItem().getType() == null) {
            return;
        }
        
        if (a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK) {
            return;
        }
        Player p = event.getPlayer();
        
        if (p == null) {
            return;
        }
        
        if (!plugin.killstreak.containsKey(p)) {
            return;
        }
        
        this.onPlayerRightClick(p, event);
    }
   

    private void onPlayerRightClick(Player p, PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        int health = plugin.getConfig().getInt("FastFoodHP");

        List<String> foodlist = plugin.getConfig().getStringList("AllowedFoods");
        
        
        if (!foodlist.contains(item.getType().toString())) {
            return;
        }
        
        if (health == 0 || !canEat(p, health)) {
            return;
        }
        
        Material type = item.getType();
        
        this.setHealth(p, health * 2);

        event.setUseItemInHand(Result.DENY);

        item = null;
        if (event.getItem().getAmount() > 1) {
            item = new ItemStack(event.getItem());
            item.setAmount(item.getAmount() - 1);
        }
        
        p.getInventory().removeItem(new ItemStack(type, 1));
    }

    
    private void setHealth(Player p, int health) {
        // Set health.        
        double newHealth = Math.min(20, p.getHealth() + health);
        p.setHealth((int) newHealth);
    }

    private boolean canEat(Player p, int value) {
        if (value > 0 && p.getHealth() >= 20) {
            return false;
        }
        if (value < 0 && p.getHealth() + value <= 0) {
            return false;
        }
        return true;
    }
}
