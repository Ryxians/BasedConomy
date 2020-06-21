package homebrew.basictrades;

import homebrew.basictrades.commands.HitC;
import homebrew.basictrades.commands.View;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public final class BasicTrades extends JavaPlugin {
    public static BasicTrades instance;

    public static Map<Inventory, HitO> sHits = new HashMap<Inventory, HitO>();

    public static Map<UUID, HitO> hits = new HashMap<UUID, HitO>();

    public static Map<UUID, HitO> eHits = new HashMap<UUID, HitO>();

    public static Inventory hitsMenu;

    public static File dataFolder;

    public FileConfiguration config = getConfig();


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        dataFolder = BasicTrades.instance.getDataFolder();

        //get config.yml
        config.options().copyDefaults(true);
        saveDefaultConfig();

        loadHits();
        hitsToMenu();
        getCommand("hit").setExecutor(new HitC());
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
        String st = (ChatColor.DARK_GREEN + "[BasedHits] " + ChatColor.GREEN + str);
        Bukkit.getOnlinePlayers().forEach(i ->
                {
                    if (i.hasPermission("BasedHits.broadcast")) {
                        i.sendMessage(st);
                    }
                }
                );
        logInfo(st);
    }

    public static void logInfo(String str) {
        Bukkit.getLogger().log(Level.INFO, str);
    }

    public static void loadHits() {
        //Get all the bounty files
        File[] bounties = getBounties();

        //Per bounty file
        for (int i = 0; i < bounties.length; i++) {
            HitO hit = new HitO(bounties[i]);
            hits.put(hit.bounty, hit);
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

        hits.values().forEach(i -> {
            i.saveHit();
        });
    }

    public static File[] getBounties() {
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

    private static void hitsToMenu() {
        int size = 27;
        if (hits.size() >= 27) size = 54;
        hitsMenu = Bukkit.createInventory(null, size, "Active Server Bounties");

        hits.values().forEach(j -> {
            if (hitsMenu.getContents().length < 53) {
                hitsMenu.addItem(j.getSkull());
            }
        });
    }

}
