package com.harry5573.ffa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.harry5573.ffa.killstreaks.Killstreak;
import com.harry5573.ffa.listeners.Blocks;
import com.harry5573.ffa.listeners.CommandBlock;
import com.harry5573.ffa.listeners.ItemDrop;
import com.harry5573.ffa.listeners.ItemPickup;
import com.harry5573.ffa.listeners.PlayerMove;

public class Main extends JavaPlugin implements Listener {

    public static Main plugin;
    
    public HashMap<Player, Integer> timer = new HashMap();
    public static HashMap<String, ItemStack[]> inventoryContents = new HashMap();
    public static HashMap<String, ItemStack[]> inventoryArmorContents = new HashMap();
    public HashMap<String, ItemStack> cursor = new HashMap();
    public HashMap<Player, Integer> killstreak = new HashMap();
    public static HashMap<Player, ArrayList<Block>> inFFA = new HashMap();
    
    public String prefix = ChatColor.WHITE + "[" + ChatColor.YELLOW + "TnTFFA" + ChatColor.WHITE + "]";

    //Start Plugin
    @Override
    public void onEnable() {
        plugin = this;

        timer.clear();
        inventoryContents.clear();
        inventoryArmorContents.clear();
        cursor.clear();
        killstreak.clear();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new Main(), this);
        pm.registerEvents(new PlayerMove(this), this);
        pm.registerEvents(new ItemDrop(this), this);
        pm.registerEvents(new ItemPickup(this), this);
        pm.registerEvents(new CommandBlock(this), this);
        pm.registerEvents(new Blocks(this), this);
        pm.registerEvents(new Killstreak(this), this);
        getLogger().info("[FFA] Started!");

        saveDefaultConfig();
    }
    //Disable Plugin 

    @Override
    public void onDisable() {
        getLogger().info("[FFA] Stopped!");
        saveConfig();
        for (Player p : Bukkit.getOnlinePlayers()) {
            disableFFA(p);
        }
    }

    //JoinFFA
    public void joinFFA(final Player p) {
        int i = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                if (Main.plugin.timer.containsKey(p)) {
                    //Save the players real world items
                    saveInventory(p);
                    saveArmor(p);
                    saveCursor(p);
                    //Send the player a message to say there stuff has been saved
                    p.sendMessage(prefix + ChatColor.GRAY + " Your old inventory has been saved");
                    //Broadcast That The Player Has Joined FFA	
                    Bukkit.broadcastMessage(prefix + ChatColor.DARK_AQUA + " " + p.getName() + " Has joined FFA! Type /ffa to join them");
                    //Remove them from the cooldown
                    plugin.timer.remove(p);
                    //Teleport them to one of the random ffa spawns
                    randomLocation(p);
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
                    //Give the players there new ffa gear	
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

                    helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                    chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                    leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                    boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

                    //Weapons (Sword and Axe)
                    ItemStack axe = new ItemStack(Material.IRON_AXE);
                    ItemStack sword = new ItemStack(Material.IRON_SWORD);
                    //Enchant The Sword And Axe
                    axe.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
                    axe.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                    sword.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
                    sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                    //Name sword/axe
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
                    //Give the players the weapons + food
                    p.getInventory().setItem(0, sword);
                    p.getInventory().setItem(1, axe);
                    givePotions(p);

                    //Set the players armour
                    p.getInventory().setHelmet(helmet);
                    p.getInventory().setChestplate(chestplate);
                    p.getInventory().setLeggings(leggings);
                    p.getInventory().setBoots(boots);
                    //Give the player a fresh set of health and hunger
                    p.setFoodLevel(20);
                    p.setHealth(20);
                    //Give the player speed 1
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
                    //Add them to the killstreak hashmap
                    plugin.killstreak.put(p, Integer.valueOf(0));
                    //Refresh There Inventorys
                    p.updateInventory();
                    //Disable teleporting for the player
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tptoggle " + p.getName() + " off");
                    //Set players exp to 0 
                    p.setLevel(0);
                    //Cool darkness potion effect
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 2));
                }
            }
        } //5 Seconds Warmup
                , 100L);
        plugin.timer.put(p, Integer.valueOf(i));
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

    //Logout in FFA
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if ((plugin.killstreak.containsKey(p))) {
            //Remove all old player potion effects
            for (PotionEffect pf : p.getActivePotionEffects()) {
                p.removePotionEffect(pf.getType());
            }
            p.setHealth(0);
            p.getInventory().setContents((ItemStack[]) inventoryContents.get(p.getName()));
            p.getInventory().setArmorContents((ItemStack[]) inventoryArmorContents.get(p.getName()));
            plugin.inventoryArmorContents.remove(p.getName());
            plugin.inventoryContents.remove(p.getName());
            plugin.cursor.remove(p);
            plugin.killstreak.remove(p);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tptoggle " + p.getName() + " on");
        }
    }

    //Disable FFA
    public void disableFFA(final Player p) {
        if ((plugin.killstreak.containsKey(p))) {
            //Remove all old player potion effects
            for (PotionEffect pf : p.getActivePotionEffects()) {
                p.removePotionEffect(pf.getType());
            }
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            p.setItemOnCursor(null);
            p.setHealth(0);
            p.getInventory().setContents((ItemStack[]) inventoryContents.get(p.getName()));
            p.getInventory().setArmorContents((ItemStack[]) inventoryArmorContents.get(p.getName()));
            plugin.inventoryArmorContents.remove(p.getName());
            plugin.inventoryContents.remove(p.getName());
            plugin.cursor.remove(p.getName());
            plugin.killstreak.remove(p);
            p.sendMessage(prefix + ChatColor.GREEN + " You were removed from FFA due to the plugin shutting down or reloading!");
        }
    }

    //Kicked in FFA
    @EventHandler
    public void onKick(PlayerKickEvent event) {
        Player p = event.getPlayer();
        if ((plugin.killstreak.containsKey(p))) {
            //Remove all old player potion effects
            for (PotionEffect pf : p.getActivePotionEffects()) {
                p.removePotionEffect(pf.getType());
            }
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tptoggle " + p.getName() + " on");
            p.setHealth(0);
            p.getInventory().setContents((ItemStack[]) inventoryContents.get(p.getName()));
            p.getInventory().setArmorContents((ItemStack[]) inventoryArmorContents.get(p.getName()));
            plugin.inventoryArmorContents.remove(p.getName());
            plugin.inventoryContents.remove(p.getName());
            plugin.cursor.remove(p.getName());
            plugin.killstreak.remove(p);
        }
    }

    //Respawn from FFA
    @EventHandler
    public void respawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        if ((plugin.killstreak.containsKey(p))) {
            //Remove all old player potion effects
            for (PotionEffect pf : p.getActivePotionEffects()) {
                p.removePotionEffect(pf.getType());
            }

            p.getInventory().setContents((ItemStack[]) inventoryContents.get(p.getName()));
            p.getInventory().setArmorContents((ItemStack[]) inventoryArmorContents.get(p.getName()));

            plugin.inventoryArmorContents.remove(p.getName());
            plugin.inventoryContents.remove(p.getName());
            plugin.cursor.remove(p.getName());
            plugin.killstreak.remove(p);
        }
    }

    //Stop them from removing armour
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (plugin.killstreak.containsKey(player)) {
            if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
                event.setCancelled(true);
                player.sendMessage(prefix + ChatColor.GREEN + " You cant remove your armour in FFA");
            }
            if (event.getSlot() == 0) {
                event.setCancelled(true);
                player.sendMessage(prefix + ChatColor.GREEN + " You cant move your sword/axe in FFA");
            }
            if (event.getSlot() == 1) {
                event.setCancelled(true);
                player.sendMessage(prefix + ChatColor.GREEN + " You cant move your sword/axe in FFA");
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
                    player.sendMessage(prefix + ChatColor.LIGHT_PURPLE + " You cannot teleport in ffa");
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player) sender;
        }

        if (command.getName().equalsIgnoreCase("ffa")) {

            if (sender == null) {
                sender.sendMessage("[FFA] You must be a player to do that!");
                return true;
            }
            if (args.length == 0) {
                p.sendMessage(prefix + ChatColor.AQUA + " Your about to join FFA! Get ready for Battle.");
                Player player = (Player) sender;
                if (p.getGameMode().equals(GameMode.CREATIVE)) {
                    p.setGameMode(GameMode.SURVIVAL);
                }
                joinFFA(player);
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("shutdown")) {
                    if (sender.hasPermission("iffa.admin")) {
                        sender.sendMessage(prefix + " " + ChatColor.RED + "No permission.");
                        return true;
                    }
                    sender.sendMessage(this.prefix + ChatColor.GREEN + " Plugin shutdown. Please RESTART the server to enable it.");
                    getServer().getPluginManager().disablePlugin(this);
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!p.hasPermission("iffa.admin")) {
                        sender.sendMessage(prefix + " " + ChatColor.RED + "No permission.");
                        return true;
                    }
                    p.sendMessage(prefix + ChatColor.GREEN + " Config reloaded");
                    System.out.println("[FFA] Config Reloading...");
                    reloadConfig();
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("setspawn")) {
                    if (!p.hasPermission("iffa.admin")) {
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
                        getConfig().set("Spawns.1", blockX + "," + blockY + "," + blockZ);
                        p.sendMessage(this.prefix + ChatColor.GOLD + " Spawn 1 set @ your location.");
                        this.saveConfig();
                    }
                    if (args[1].equals("2")) {
                        getConfig().set("Spawns.2", blockX + "," + blockY + "," + blockZ);
                        p.sendMessage(this.prefix + ChatColor.GOLD + " Spawn 2 set @ your location.");
                        this.saveConfig();
                    }
                    if (args[1].equals("3")) {
                        getConfig().set("Spawns.3", blockX + "," + blockY + "," + blockZ);
                        p.sendMessage(this.prefix + ChatColor.GOLD + " Spawn 3 set @ your location.");
                        this.saveConfig();
                    }
                    if (args[1].equals("4")) {
                        getConfig().set("Spawns.4", blockX + "," + blockY + "," + blockZ);
                        p.sendMessage(this.prefix + ChatColor.GOLD + " Spawn 4 set @ your location.");
                        this.saveConfig();
                    }
                }
                if (args[0].equalsIgnoreCase("removespawn")) {
                    if (!p.hasPermission("iffa.admin")) {
                        sender.sendMessage(prefix + " " + ChatColor.RED + "No permission.");
                        return true;
                    }
                    if (args[1].equals("1")) {
                        getConfig().set("Spawns.1", " ");
                        p.sendMessage(this.prefix + ChatColor.GOLD + " Spawn 1 set to null.");
                        this.saveConfig();
                    }
                    if (args[1].equals("2")) {
                        getConfig().set("Spawns.2", " ");
                        p.sendMessage(this.prefix + ChatColor.GOLD + " Spawn 2 set to null.");
                        this.saveConfig();
                    }
                    if (args[1].equals("3")) {
                        getConfig().set("Spawns.3", " ");
                        p.sendMessage(this.prefix + ChatColor.GOLD + " Spawn 3 set to null.");
                        this.saveConfig();
                    }
                    if (args[1].equals("4")) {
                        getConfig().set("Spawns.4", " ");
                        p.sendMessage(this.prefix + ChatColor.GOLD + " Spawn 4 set to null.");
                        this.saveConfig();
                    }
                }
            }
        }
        return false;
    }

    public void randomLocation(Player p) {
        Random random = new Random();
        switch (random.nextInt(3)) {
            case 0:
                //Spawn 1
                Location l = null;
                String spawn1 = (String) getConfig().get("Spawns.1");
                String[] spawnfinal1 = spawn1.split(",");
                if (spawnfinal1.length == 3) {
                    int x = Integer.parseInt(spawnfinal1[0]);
                    int y = Integer.parseInt(spawnfinal1[1]);
                    int z = Integer.parseInt(spawnfinal1[2]);
                    l = new Location(getServer().getWorld(p.getWorld().getName()), x, y, z);
                }
                p.teleport(l, PlayerTeleportEvent.TeleportCause.COMMAND);
                break;
            case 1:
                //Spawn 2
                Location l2 = null;
                String spawn2 = (String) getConfig().get("Spawns.2");
                String[] spawnfinal2 = spawn2.split(",");
                if (spawnfinal2.length == 3) {
                    int x = Integer.parseInt(spawnfinal2[0]);
                    int y = Integer.parseInt(spawnfinal2[1]);
                    int z = Integer.parseInt(spawnfinal2[2]);
                    l2 = new Location(getServer().getWorld(p.getWorld().getName()), x, y, z);
                }
                p.teleport(l2, PlayerTeleportEvent.TeleportCause.COMMAND);
                break;
            case 2:
                //Spawn 3
                Location l3 = null;
                String spawn3 = (String) getConfig().get("Spawns.3");
                String[] spawnfinal3 = spawn3.split(",");
                if (spawnfinal3.length == 3) {
                    int x = Integer.parseInt(spawnfinal3[0]);
                    int y = Integer.parseInt(spawnfinal3[1]);
                    int z = Integer.parseInt(spawnfinal3[2]);
                    l3 = new Location(getServer().getWorld(p.getWorld().getName()), x, y, z);
                }
                p.teleport(l3, PlayerTeleportEvent.TeleportCause.COMMAND);
                break;
            case 3:
                //Spawn 4
                Location l4 = null;
                String spawn4 = (String) getConfig().get("Spawns.4");
                String[] spawnfinal4 = spawn4.split(",");
                if (spawnfinal4.length == 3) {
                    int x = Integer.parseInt(spawnfinal4[0]);
                    int y = Integer.parseInt(spawnfinal4[1]);
                    int z = Integer.parseInt(spawnfinal4[2]);
                    l4 = new Location(getServer().getWorld(p.getWorld().getName()), x, y, z);
                }
                p.teleport(l4, PlayerTeleportEvent.TeleportCause.COMMAND);
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
        plugin.cursor.put(p.getName(), p.getItemOnCursor());
    }

    public void saveInventory(Player p) {
        System.out.println("[FFA] Saving players inventory...");
        plugin.inventoryContents.put(p.getName(), p.getInventory().getContents());
    }
}
