package homebrew.basictrades;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public class HitO {
    //Important things relative to the hit
    public UUID owner;
    public UUID bounty;
    public Inventory prize;
    private HitExpireTask task;

    public HitO(OfflinePlayer bountyOwner, OfflinePlayer bounty, Inventory inventory) {
        if (bountyOwner == null) {
            owner = null;
        } else {
            owner = bountyOwner.getUniqueId();
        }

        if (bounty == null) {
            Bukkit.getLogger().log(Level.WARNING, "This bounty is null");
        } else {
            this.bounty = bounty.getUniqueId();
        }

        prize = inventory;
        try {
            long temp = BasicTrades.instance.config.getLong("expiration");
            task = new HitExpireTask(this, temp);
        } catch (Exception ex) {
            BasicTrades.logInfo("Config Expiration failed to load, expiration disabled.");
            ex.printStackTrace();
        }
    }

    public HitO(File loadHit) {
        //Open config
        ConfigManagement configMan = new ConfigManagement(loadHit);
        FileConfiguration savedHit = configMan.getConfig();

        //delay for expiration
        long delay = 24000;

        //Get bounty
        bounty = fileToUUID(loadHit.getName());

        //Get inventory and owner
        Map<String, Object> tem = savedHit.getValues(true);
        ItemStack[] is = new ItemStack[27];
        for (Map.Entry<String, Object> ent : tem.entrySet()) {
            String key = ent.getKey();
            Object value = ent.getValue();

            //Grab owner
            switch (key) {
                case "Owner":
                    owner = UUID.fromString((String) value);
                    break;
                case "Expiration":
                    //String temp = (String) value;
                    //delay = Long.parseLong(temp);
                    delay = savedHit.getLong(key);
                    break;
                default:
                    if (value instanceof ItemStack) {
                        ItemStack item = (ItemStack) value;
                        int spot = Integer.parseInt(key.substring(10));
                        is[spot] = item;
                    }
                    break;
            }
        }
        prize = Bukkit.createInventory(null, 27, "Bounty");
        prize.setContents(is);
        task = new HitExpireTask(this, delay);
    }

    public OfflinePlayer getOwner() {
        OfflinePlayer owner;
        if (this.owner != null) {
            owner = Bukkit.getOfflinePlayer(this.owner);
        } else {
            owner = null;
        }
        return owner;
    }

    public String getBountyName() {
        return getBounty().getName();
    }

    public OfflinePlayer getBounty() {
        return Bukkit.getOfflinePlayer(bounty);
    }

    public ItemStack getSkull() {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(bounty));
        List<String> lore = new ArrayList<String>();
        if (owner != null) {
            lore.add("Bounty Owner: " + getOwner().getName());
        }
        meta.setLore(lore);
        skull.setItemMeta(meta);
        return skull;
    }

    public void saveHit() {
        saveHit("Hits" + File.separator + bounty + ".yml");
    }

    public void saveHit(String fileName) {
        ConfigManagement configMan = new ConfigManagement(fileName);
        FileConfiguration savedHit = configMan.getConfig();

        savedHit.set("Expiration", Long.valueOf(task.delay));

        //Save owner
        if (owner != null) {
            savedHit.set("Owner", owner.toString());
        }

        //Save Inventory
        ItemStack[] contents = prize.getContents();
        for (int i = 0; i < contents.length; i++) {
            savedHit.set("Inventory." + String.valueOf(i), contents[i]);
        }
        configMan.saveConfig();
    }

    //private methods
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
}
