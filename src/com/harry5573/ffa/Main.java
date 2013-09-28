package com.harry5573.ffa;

import com.gmail.nossr50.api.AbilityAPI;
import com.harry5573.ffa.killstreaks.Killstreak;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.harry5573.ffa.listeners.EventListener;
import com.harry5573.food.FastFood;
import com.harry5573.items.ItemGiver;
import com.harry5573.items.ItemHandler;
import com.harry5573.region.Region;
import com.harry5573.region.RegionUtils;
import com.harry5573.region.Selection;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.util.BlockVector;

public class Main extends JavaPlugin implements Listener {

    public static Main plugin;
    
    private Region ffaregion;
    
    public HashMap<Player, Integer> timer = new HashMap();
    public static HashMap<String, ItemStack[]> inventoryContents = new HashMap();
    public static HashMap<String, ItemStack[]> inventoryArmorContents = new HashMap();
    public HashMap<String, ItemStack> cursor = new HashMap();
    public HashMap<Player, Integer> killstreak = new HashMap();
    
    public static HashMap<Player, ArrayList<Block>> inFFA = new HashMap();
    
    private String prefix = null;
    public EventListener elistener;
    public ItemHandler ih;
    public RegionUtils rus;
    
    public static Economy econ = null;

    @Override
    public void onEnable() {
        plugin = this;
        
        timer.clear();
        inventoryContents.clear();
        inventoryArmorContents.clear();
        cursor.clear();
        killstreak.clear();

        this.elistener = new EventListener(this);
        this.ih = new ItemHandler(this);
        this.rus = new RegionUtils(this);

        PluginManager pm = getServer().getPluginManager();
        
        pm.registerEvents(new EventListener(this), this);
        pm.registerEvents(new Killstreak(this), this);
        pm.registerEvents(new FastFood(this), this);
        pm.registerEvents(new ItemGiver(this), this);
        
        System.out.println("[FFA] Plugin Has Been Started!");

        this.saveDefaultConfig();
        
        ffaregion = this.rus.getFFARegion();

        prefix = getConfig().getString("prefix").replaceAll("(&([a-f0-9]))", "\u00A7$2");
        
        //NEW Vault Intergration
        Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
        Plugin essentials = Bukkit.getPluginManager().getPlugin("Essentials");
        Plugin mcmmo = Bukkit.getPluginManager().getPlugin("mcMMO");

        if (vault == null) {
            System.out.println("[FFA] We could not find vault. Plugin disabled");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (essentials == null) {
            System.out.println("[FFA] We could not find Essentials. Plugin disabled");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (mcmmo == null) {
            System.out.println("[FFA] We could not find mcMMO. Plugin disabled");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.setupEconomy();
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            this.removeFromFFA(p);
        }

        System.out.println("[FFA] Plugin Has Been Stopped And All Players In FFA Removed!");
        saveConfig();
    }
    
    public Region getFFARegion() {
        return ffaregion;
    }
    
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public void giveMoney(Player p, double amount) {
        p.sendMessage(ChatColor.GREEN + "$" + amount + ChatColor.GREEN + " has been added to your account.");
        econ.depositPlayer(p.getName(), amount);
    }

    //JoinFFA
    public void joinFFA(final Player p) {
        p.getOpenInventory().close();
        p.setItemOnCursor(new ItemStack(Material.AIR));

            //Save the players real world items
            this.saveInventory(p);
            this.saveArmor(p);
            this.saveCursor(p);
            //Send the player a message to say there stuff has been saved
            p.sendMessage(prefix + ChatColor.GRAY + " Your old inventory has been saved");

            //Give them there new items and potions
            ih.giveItems(p);
            ih.giveFood(p);

            Bukkit.broadcastMessage(prefix + ChatColor.DARK_AQUA + " " + p.getName() + " Has joined FFA! Type /ffa to join them");

            plugin.timer.remove(p);

            this.randomLocation(p);

            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));

            plugin.killstreak.put(p, Integer.valueOf(0));

            p.updateInventory();

            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "fly " + p.getName() + " off");

            p.setLevel(0);
            p.setAllowFlight(false);
            
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 2));
    }

    public void removeFromFFA(final Player p) {
        if ((plugin.killstreak.containsKey(p))) {

            for (PotionEffect pf : p.getActivePotionEffects()) {
                p.removePotionEffect(pf.getType());
            }
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);

            if (p.getItemOnCursor() == null) {
                return;
            }
            p.setItemOnCursor(null);

            p.setHealth(0);

            restorePlayerStuff(p);

            plugin.inventoryArmorContents.remove(p.getName());
            plugin.inventoryContents.remove(p.getName());
            plugin.cursor.remove(p.getName());
            plugin.killstreak.remove(p);

            p.sendMessage(prefix + ChatColor.GREEN + " You were removed from FFA");
        }
    }

    public void respawnFFA(final Player p) {
        if (plugin.killstreak.containsKey(p)) {

            for (PotionEffect pf : p.getActivePotionEffects()) {
                p.removePotionEffect(pf.getType());
            }
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);

            if (p.getItemOnCursor() == null) {
                return;
            }
            p.setItemOnCursor(null);

            this.restorePlayerStuff(p);

            plugin.inventoryArmorContents.remove(p.getName());
            plugin.inventoryContents.remove(p.getName());
            plugin.cursor.remove(p.getName());
            plugin.killstreak.remove(p);
        }
    }

    public void preJoinFFA(final Player p) {
        p.sendMessage(prefix + ChatColor.AQUA + " Your about to join FFA! Get ready for Battle.");
        if (p.getGameMode().equals(GameMode.CREATIVE)) {
            p.setGameMode(GameMode.SURVIVAL);
        }
        int i = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                plugin.joinFFA(p);
            }
        }, plugin.getConfig().getInt("warmuptime") * 20);
        plugin.timer.put(p, Integer.valueOf(i));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player) sender;
        }

        if (command.getName().equalsIgnoreCase("ffa")) {

            if (p == null) {
                sender.sendMessage("[FFA] You must be a player to do that!");
                return true;
            }
            
            if (args.length == 0) {
              this.preJoinFFA(p);
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("shutdown")) {
                    if (sender.hasPermission("ffa.admin")) {
                        sender.sendMessage(prefix + " " + ChatColor.RED + "No permission.");
                        return true;
                    }
                    sender.sendMessage(this.prefix + ChatColor.GREEN + " Plugin shutdown. Please RESTART the server to enable it.");
                    getServer().getPluginManager().disablePlugin(this);
                }
                if (args[0].equalsIgnoreCase("define")) {
                    if (!sender.hasPermission("ffa.admin")) {
                        sender.sendMessage(prefix + " " + ChatColor.RED + "No permission.");
                        return true;
                    }
                    BlockVector p1 = Selection.getP1();
                    BlockVector p2 = Selection.getP2();
                    if (p1 != null && p2 != null) {
                        this.setRegion(p.getWorld(), p1, p2);
                        ffaregion = this.rus.getFFARegion();
                        sender.sendMessage(this.getPrefix() + ChatColor.RED + " Region has been defined.");
                        return true;
                    } else {
                        sender.sendMessage(this.getPrefix() + ChatColor.RED + " You need to select both points");
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!p.hasPermission("ffa.admin")) {
                        sender.sendMessage(prefix + " " + ChatColor.RED + "No permission.");
                        return true;
                    }
                    p.sendMessage(prefix + ChatColor.GREEN + " Config reloaded");
                    System.out.println("[FFA] Config Reloading...");
                    reloadConfig();
                    return true;
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("setspawn")) {
                    if (!p.hasPermission("ffa.admin")) {
                        sender.sendMessage(prefix + " " + ChatColor.RED + "No permission.");
                        return true;
                    }
                    //Get The Players Location
                    Location loc = p.getLocation();
                    int blockX = loc.getBlockX();
                    int blockY = loc.getBlockY();
                    int blockZ = loc.getBlockZ();
                    //Set The Spawn
                    if (args[1].equals("1")) {
                        this.setWorld(p, p.getWorld());
                        this.setSpawn(p, blockX, blockY, blockZ, 1);
                        return true;
                    }
                    if (args[1].equals("2")) {
                        this.setWorld(p, p.getWorld());
                        this.setSpawn(p, blockX, blockY, blockZ, 2);
                        return true;
                    }
                    if (args[1].equals("3")) {
                        this.setWorld(p, p.getWorld());
                        this.setSpawn(p, blockX, blockY, blockZ, 3);
                        return true;
                    }
                    if (args[1].equals("4")) {
                        this.setWorld(p, p.getWorld());
                        this.setSpawn(p, blockX, blockY, blockZ, 4);
                        return true;
                    } else {
                        p.sendMessage(this.prefix + ChatColor.RED + " Usage: /ffa setspawn <1/4>");
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("removespawn")) {
                    if (!p.hasPermission("iffa.admin")) {
                        sender.sendMessage(prefix + " " + ChatColor.RED + "No permission.");
                        return true;
                    }
                    if (args[1].equals("1")) {
                        this.removeSpawn(1);
                        return true;
                    }
                    if (args[1].equals("2")) {
                        this.removeSpawn(2);
                        return true;
                    }
                    if (args[1].equals("3")) {
                        this.removeSpawn(3);
                        return true;
                    }
                    if (args[1].equals("4")) {
                        this.removeSpawn(4);
                        return true;
                    } else {
                        p.sendMessage(this.prefix + ChatColor.RED + " Usage: /ffa removespawn <1/4>");
                        return true;
                    }
                } else {
                    p.sendMessage(this.prefix + ChatColor.RED + " Usage: /ffa set||removespawn <1/4>");
                }
            }
        }
        return false;
    }

    public void removeSpawn(int id) {
        this.getConfig().set("Spawns." + id, " ");
        this.saveConfig();
    }

    public void setSpawn(Player p, int blockX, int blockY, int blockZ, int spawnid) {
        this.getConfig().set("Spawns." + spawnid, blockX + "," + blockY + "," + blockZ);
        p.sendMessage(this.getPrefix() + ChatColor.GOLD + " Spawn " + spawnid + " set @ your location.");
        this.saveConfig();
    }
    
    public void setWorld(Player p, World world) {
        p.sendMessage(this.getPrefix() + ChatColor.AQUA + " World has been set to " + world.getName());
        this.getConfig().set("Spawns.world", world.getName());
    }
    
    public void teleportPlayer(Player p, Location loc) {
        p.teleport(loc, PlayerTeleportEvent.TeleportCause.COMMAND);
    }
    
    public World getFFAWorld() {
        World world = Bukkit.getWorld(this.getConfig().getString("Spawns.world"));
        return world;
    }

    public void randomLocation(Player p) {
        Random random = new Random();
        switch (random.nextInt(3)) {
            case 0:
                //Spawn 1
                Location l = null;
                String spawn1 = getConfig().getString("Spawns.1");
                String[] spawnfinal1 = spawn1.split(",");
                if (spawnfinal1.length == 3) {
                    int x = Integer.parseInt(spawnfinal1[0]);
                    int y = Integer.parseInt(spawnfinal1[1]);
                    int z = Integer.parseInt(spawnfinal1[2]);
                    l = new Location(this.getFFAWorld(), x, y, z);
                }
                this.teleportPlayer(p, l);
                break;
            case 1:
                //Spawn 2
                Location l2 = null;
                String spawn2 = getConfig().getString("Spawns.2");
                String[] spawnfinal2 = spawn2.split(",");
                if (spawnfinal2.length == 3) {
                    int x = Integer.parseInt(spawnfinal2[0]);
                    int y = Integer.parseInt(spawnfinal2[1]);
                    int z = Integer.parseInt(spawnfinal2[2]);
                    l2 = new Location(this.getFFAWorld(), x, y, z);
                }
                this.teleportPlayer(p, l2);
                break;
            case 2:
                //Spawn 3
                Location l3 = null;
                String spawn3 = getConfig().getString("Spawns.3");
                String[] spawnfinal3 = spawn3.split(",");
                if (spawnfinal3.length == 3) {
                    int x = Integer.parseInt(spawnfinal3[0]);
                    int y = Integer.parseInt(spawnfinal3[1]);
                    int z = Integer.parseInt(spawnfinal3[2]);
                    l3 = new Location(this.getFFAWorld(), x, y, z);
                }
                this.teleportPlayer(p, l3);
                break;
            case 3:
                //Spawn 4
                Location l4 = null;
                String spawn4 = getConfig().getString("Spawns.4");
                String[] spawnfinal4 = spawn4.split(",");
                if (spawnfinal4.length == 3) {
                    int x = Integer.parseInt(spawnfinal4[0]);
                    int y = Integer.parseInt(spawnfinal4[1]);
                    int z = Integer.parseInt(spawnfinal4[2]);
                    l4 = new Location(this.getFFAWorld(), x, y, z);
                }
                this.teleportPlayer(p, l4);
                break;
            default:
                break;
        }
    }

    public void saveArmor(Player p) {
        System.out.println("[FFA] Saving players armour...");
        plugin.inventoryArmorContents.put(p.getName(), p.getInventory().getArmorContents());
    }

    public void saveCursor(Player p) {
        System.out.println("[FFA] Saving players armour...");

        if (p.getItemOnCursor() == null) {
            return;
        }
        plugin.cursor.put(p.getName(), p.getItemOnCursor());
    }

    public void saveInventory(Player p) {
        System.out.println("[FFA] Saving players inventory...");
        plugin.inventoryContents.put(p.getName(), p.getInventory().getContents());
    }

    public void restorePlayerStuff(Player p) {
        p.getInventory().clear();
        p.getInventory().setContents((ItemStack[]) inventoryContents.get(p.getName()));
        p.getInventory().setArmorContents((ItemStack[]) inventoryArmorContents.get(p.getName()));

        if (cursor.containsKey(p.getName())) {
            p.getInventory().addItem((ItemStack) cursor.get(p.getName()));
        }
    }
    
    public String getPrefix() {
        return this.prefix;
    }

    public void setRegion(World world, BlockVector p1, BlockVector p2) {
        this.getConfig().set("region.world", world.getName());
        this.getConfig().set("region.p1.x", p1.getX());
        this.getConfig().set("region.p1.y", 0);
        this.getConfig().set("region.p1.z", p1.getZ());
        this.getConfig().set("region.p2.x", p2.getX());
        this.getConfig().set("region.p2.y", 256);
        this.getConfig().set("region.p2.z", p2.getZ());
        this.saveConfig();
    }
}
