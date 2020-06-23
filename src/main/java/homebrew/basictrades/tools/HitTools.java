package homebrew.basictrades.tools;

import homebrew.basictrades.BasicTrades;
import homebrew.basictrades.hit.HitO;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HitTools {

    public static void loadHits() {
        //Get all the bounty files
        File[] bounties = getBounties();

        //Per bounty file
        for (int i = 0; i < bounties.length; i++) {
            HitO hit = new HitO(bounties[i]);
            BasicTrades.hits.put(hit.bounty, hit);
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
            if (!BasicTrades.hits.containsKey(fileToUUID(bounties[i].getName()))) {
                bounties[i].delete();
            }
        }

        BasicTrades.hits.values().forEach(i -> {
            i.saveHit();
        });
    }

    public static File[] getBounties() {
        //Get the hits folder with all the hit files
        File hitsFolder = new File(BasicTrades.dataFolder, "Hits" + File.separator);
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
        if (BasicTrades.hits.size() >= 27) size = 54;
        BasicTrades.hitsMenu = Bukkit.createInventory(null, size, "Active Server Bounties");

        BasicTrades.hits.values().forEach(j -> {
            if (BasicTrades.hitsMenu.getContents().length < 53) {
                BasicTrades.hitsMenu.addItem(j.getSkull());
            }
        });
    }

    public static boolean isInventoryEmpty(Inventory inv) {
        boolean isEmpty = true;
        for (ItemStack i : inv.getContents()) {
            if (i != null) {
                isEmpty = false;
            }
        }
        return isEmpty;
    }
}
