package homebrew.basictrades.tools;

import homebrew.basictrades.BasicTrades;
import homebrew.basictrades.hit.HitE;
import homebrew.basictrades.hit.HitO;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HitTools {

    public static void loadHits() {
        //Get all the bounty files
        File[] bounties = getBounties("Hits");

        //Per bounty file
        for (int i = 0; i < bounties.length; i++) {
            HitO hit = new HitO(bounties[i]);
            BasicTrades.hits.put(hit.getBountyUUID(), hit);
        }

    }

    public static void loadExpiredHit(OfflinePlayer bountyOwner, OfflinePlayer bounty, Inventory inventory) {
        HitE expiredHit = new HitE(bountyOwner, bounty, inventory);
        loadExpiredHit(expiredHit);
    }

    public static void loadExpiredHit(HitE hit) {
        List<HitE> hits;
        if (BasicTrades.eHits.get(hit.getBountyUUID()) == null) {
            hits = new ArrayList<HitE>();
        } else {
            hits = BasicTrades.eHits.get(hit.getBountyUUID());
        }
        hits.add(hit);
        BasicTrades.eHits.put(hit.getOwnerUUID(), hits);
    }

    public static void loadExpiredHits() {
        //Get all the expired hits
        File[] expiredHits = getBounties("Expired");

        //Per bounty file
        for (int i = 0; i < expiredHits.length; i++) {
            HitE hit = new HitE(expiredHits[i]);
            loadExpiredHit(hit);
        }
    }

    public static void saveExpiredHits() {
        //Get expired hits
        deleteBounties("Expired");

        BasicTrades.eHits.forEach((key, value) -> {
            AtomicInteger j = new AtomicInteger();
            value.forEach(i -> {
                i.saveHit(j.get());
                j.getAndIncrement();
            });
        });
    }

    public static List<HitE> getExpiredHits(UUID username) {
        List<HitE> hits = BasicTrades.eHits.get(username);
        return hits;
    }

    public static void save() {
        saveHits();
        saveExpiredHits();
        hitsToMenu();
    }
    public static void saveHits() {
        deleteBounties("Hits");

        BasicTrades.hits.values().forEach(i -> {
            i.saveHit();
        });
    }

    public static void deleteBounties(String folder) {
        //Get the bounties
        File[] bounties = getBounties(folder);

        //Delete bounty files from completed hits
        for (int i = 0; i < bounties.length; i++) {
            if (!BasicTrades.hits.containsKey(fileToUUID(bounties[i].getName()))) {
                bounties[i].delete();
            }
        }
    }

    public static File[] getBounties(String folder) {
        //Get the hits folder with all the hit files
        File hitsFolder = new File(BasicTrades.dataFolder, folder + File.separator);
        File[] bounties = hitsFolder.listFiles();
        if (bounties == null) bounties = new File[0];
        return bounties;
    }

    public static UUID fileToUUID(String s) {
        //Build the string of file.yml without .yml
        char[] arr = s.toCharArray();
        String name = "";
        for (int j = 0; j < 36; j++) {
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
