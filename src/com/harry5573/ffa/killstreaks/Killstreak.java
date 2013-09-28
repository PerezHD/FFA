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

public class Killstreak implements Listener {
    
    Main plugin;

    public Killstreak(Main instance) {
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

            //If its a killstreak then we carry on
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
