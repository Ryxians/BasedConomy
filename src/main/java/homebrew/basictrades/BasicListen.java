package homebrew.basictrades;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class BasicListen implements Listener {

    public static Map<Inventory, HitO> sHits = BasicTrades.sHits;

    public static Map<UUID, HitO> hits = BasicTrades.hits;

    @EventHandler
    public void hitCreate(InventoryCloseEvent evt) {
        if (sHits.containsKey(evt.getInventory())) {
            boolean isEmpty = true;
            for (ItemStack i : evt.getInventory().getContents()) {
                if (i != null) {
                    isEmpty = false;
                }
            }
            if (isEmpty) {
                sHits.remove(evt.getInventory());
            } else {
                HitO hit = sHits.remove(evt.getInventory());
                hits.put(hit.bounty, hit);

                //Determine whether the entire server gets an anouncement
                if (evt.getPlayer().hasPermission("BasedHits.anonymous.hit")) {
                    BasicTrades.success("An anonymous player has placed a bounty on " + Bukkit.getOfflinePlayer(hit.bounty).getName() + ".");
                    hit.owner = null;
                } else {
                    BasicTrades.success(evt.getPlayer().getName() + " has placed a bounty on " + Bukkit.getOfflinePlayer(hit.bounty).getName() + ".");
                }

                BasicTrades.save();
            }
        }
    }

    @EventHandler
    public void hitView(InventoryClickEvent evt) {
        if (checkForInventory(evt.getInventory()) || BasicTrades.hitsMenu == evt.getInventory()) {
            if (BasicTrades.hitsMenu == evt.getInventory()) {
                if ((evt.getCurrentItem() != null) && evt.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                    SkullMeta meta = (SkullMeta) evt.getCurrentItem().getItemMeta();
                    UUID uuid = meta.getOwningPlayer().getUniqueId();
                    evt.getView().getPlayer().openInventory(hits.get(uuid).prize);
                }
            }
            evt.setCancelled(true);
        }
    }
    @EventHandler
    public void hitView(InventoryDragEvent evt) {
        if (checkForInventory(evt.getInventory()) || BasicTrades.hitsMenu == evt.getInventory()) {
            evt.setCancelled(true);
        }
    }

    private boolean checkForInventory(Inventory inv) {
        AtomicBoolean rc = new AtomicBoolean(false);
        hits.values().forEach(i -> {
            if (i.prize == inv) {
                rc.set(true);
            }
        });
        return rc.get();
    }

    @EventHandler
    public void hitDeath(PlayerDeathEvent evt) {
        Player died = evt.getEntity();
        Player killer = died.getKiller();
        if (killer != null) {
            if (killer != died && killer.hasPermission("BasedHits.claim")) {
                if (hits.containsKey(died.getUniqueId())) {
                    Inventory death = hits.remove(died.getUniqueId()).prize;
                    for (ItemStack i : death.getContents()) {
                        evt.getDrops().add(i);
                    }
                    if (killer.hasPermission("BasedHits.anonymous.claim")) {
                        BasicTrades.success("The bounty on " + died.getName() + " has been claimed.");
                    } else {
                        BasicTrades.success(evt.getDeathMessage() + ", thus claiming the Bounty.");
                    }
                    BasicTrades.save();
                    evt.setDeathMessage(null);
                }
            }
        }
    }
}
