package homebrew.basictrades;

import homebrew.basictrades.commands.Hit;
import homebrew.basictrades.commands.View;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public final class BasicTrades extends JavaPlugin {
    public static BasicTrades instance;

    public static Map<Inventory, UUID> sHits = new HashMap<Inventory, UUID>();

    public static Map<UUID, Inventory> hits = new HashMap<UUID, Inventory>();

    public static Inventory hitsMenu;

    public static File dataFolder;


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        dataFolder = BasicTrades.instance.getDataFolder();
        loadHits();
        hitsToMenu();
        getCommand("hit").setExecutor(new Hit());
        getCommand("view").setExecutor(new View());
        getServer().getPluginManager().registerEvents(new BasicListen(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveHits();
    }

    public static void success(CommandSender sender, String str) {
        sender.sendMessage(ChatColor.DARK_GREEN + "[BasedHits] " + ChatColor.GREEN + str);
    }

    public static void fail(CommandSender sender, String str) {
        sender.sendMessage(ChatColor.DARK_RED + "[BasedHits] " + ChatColor.RED + str);
    }

    public static void success(String str) {
        Bukkit.getServer().broadcastMessage(ChatColor.DARK_GREEN + "[BasedHits] " + ChatColor.GREEN + str);
    }

    public static void loadHits() {
        //Get all the bounty files
        File[] bounties = getBounties();

        //Per bounty file
        for (int i = 0; i < bounties.length; i++) {
            //Get uuid from file name
            FileConfiguration userHit = YamlConfiguration.loadConfiguration(bounties[i]);
            UUID uuid = fileToUUID(bounties[i].getName());
            Map<String, Object> tem = userHit.getValues(false);
            ItemStack[] is = new ItemStack[27];
            for (Map.Entry<String, Object> ent : tem.entrySet()) {
                //Make each item for the inventory
                if (ent.getValue() instanceof ItemStack) {
                    String s = ent.getKey();
                    ItemStack item = (ItemStack) ent.getValue();
                    int spot = Integer.parseInt(s);
                    is[spot] = item;
                }
            }
            Inventory inv = Bukkit.createInventory(Bukkit.getPlayer(uuid), 27, "Hit Price");
            inv.setContents(is);
            hits.put(uuid, inv);
        }

    }
    public static void save() {
        saveHits();
        hitsToMenu();
    }
    public static void saveHits() {
        //Get the bounties
        File[] bounties = getBounties();

        //Delete bounty files from completed hits
        for (int i = 0; i < bounties.length; i++) {
            if (!hits.containsKey(fileToUUID(bounties[i].getName()))) {
                bounties[i].delete();
            }
        }

        for (Map.Entry<UUID, Inventory> entry : hits.entrySet()) {
            //Grab the current UUID and Inventory per hit
            UUID uuid = entry.getKey();
            Inventory inventory = entry.getValue();

            //Go to file
            String fileName = "Hits" + File.separator + uuid + ".yml";
            File configFile = new File(dataFolder, fileName);
            FileConfiguration userHit = YamlConfiguration.loadConfiguration(configFile);

            //Get everything in the inventory
            ItemStack[] contents = inventory.getContents();
            for (int i = 0; i < contents.length; i++) {
                if (contents[i] != null) {
                    userHit.set(String.valueOf(i), contents[i]);
                }
            }

            //Save the config for this player
            try {
                userHit.save(configFile);
            } catch (IOException ex) {
                BasicTrades.instance.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
            }

        }
    }

    private static File[] getBounties() {
        //Get the hits folder with all the hit files
        File hitsFolder = new File(dataFolder, "Hits" + File.separator);
        File[] bounties = hitsFolder.listFiles();
        if (bounties == null) bounties = new File[0];
        return bounties;
    }

    private static UUID fileToUUID(String s) {
        //Build the string of file.yml without .yml
        char[] arr = s.toCharArray();
        String name = "";
        for (int j = 0; j < arr.length - 4; j++) {
            name += arr[j];
        }
        UUID uuid = UUID.fromString(name);
        return uuid;
    }

    public static void hitsToMenu() {
        int size = 27;
        int i = 0;
        if (hits.size() >= 27) size = 54;
        hitsMenu = Bukkit.createInventory(null, size, "Active Server Bounties");

        for (Map.Entry<UUID, Inventory> entry : hits.entrySet()) {
            //uuid of the player with bounty over their head
            UUID uuid = entry.getKey();

            //Bounty
            Inventory inv = entry.getValue();

            //Make player skull
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
            skull.setItemMeta(meta);

            //Add skull to menu
            hitsMenu.addItem(skull);

            //This is to stop the inventory from getting full
            if (i > 53) {
                break;
            }
            i++;
        }
    }

}
