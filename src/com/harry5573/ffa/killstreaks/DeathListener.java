package com.harry5573.ffa.killstreaks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.harry5573.ffa.Main;
import com.harry5573.ffa.api.PlayerKillstreakEvent;

public class DeathListener implements Listener {
    
    Main plugin;

    public DeathListener(Main instance) {
        this.plugin = instance;
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event) {
        Entity e = event.getEntity();
        if ((e instanceof Player) && (plugin.killstreak.containsKey((Player) e))) {

            event.getDrops().clear();

            Player killer = ((Player) e).getKiller();

            event.setDroppedExp(0);
            event.setDeathMessage(null);
            
            if (killer != null) {

                int x = ((Integer) plugin.killstreak.get(killer)).intValue();
                int newx = x + 1;

                plugin.killstreak.remove(killer);
                plugin.killstreak.put(killer, Integer.valueOf(newx));

                PlayerKillstreakEvent killstreakevent = new PlayerKillstreakEvent(killer, newx);
                Bukkit.getServer().getPluginManager().callEvent(killstreakevent);
            }
        }
    }
}
