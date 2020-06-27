package homebrew.basictrades;

import homebrew.basictrades.hit.HitE;
import homebrew.basictrades.hit.HitO;
import homebrew.basictrades.tools.HitTools;
import homebrew.basictrades.tools.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class BasicListen implements Listener {

    public static Map<Inventory, HitO> sHits = BasicTrades.sHits;

    public static Map<UUID, HitO> hits = BasicTrades.hits;

    public static Map<UUID, List<HitE>> eHits = BasicTrades.eHits;

    @EventHandler
    public void hitCreate(InventoryCloseEvent evt) {
        if (sHits.containsKey(evt.getInventory())) {
            boolean isEmpty = HitTools.isInventoryEmpty(evt.getInventory());
            if (isEmpty) {
                sHits.remove(evt.getInventory());
            } else {
                HitO hit = sHits.remove(evt.getInventory());
                hits.put(hit.getBountyUUID(), hit);

                //Determine whether the entire server gets an anouncement
                if (hit.getOwnerUUID() == null) {
                    Messages.success("An anonymous player has placed a bounty on " + Bukkit.getOfflinePlayer(hit.getBountyUUID()).getName() + ".");
                } else {
                    Messages.success(evt.getPlayer().getName() + " has placed a bounty on " + Bukkit.getOfflinePlayer(hit.getBountyUUID()).getName() + ".");
                }

                HitTools.save();
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
                    evt.getView().getPlayer().openInventory(hits.get(uuid).getPrize());
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
            if (i.getPrize() == inv) {
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
                    Inventory death = hits.remove(died.getUniqueId()).getPrize();
                    for (ItemStack i : death.getContents()) {
                        evt.getDrops().add(i);
                    }
                    if (killer.hasPermission("BasedHits.anonymous.claim")) {
                        Messages.success("The bounty on " + died.getName() + " has been claimed.");
                    } else {
                        Messages.success(evt.getDeathMessage() + ", thus claiming the Bounty.");
                    }
                    HitTools.save();
                    evt.setDeathMessage(null);
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent evt) {
        Player player = evt.getPlayer();
        if (hits.containsKey(player.getUniqueId())) {
            HitO hit = hits.get(player.getUniqueId());
            if (hit.getOwnerUUID() != null) {
                Messages.success(player, hit.getOwner().getName() + " has an active bounty on your head.");
            } else {
                Messages.success("An active bounty is over your head.");
            }
        }
        if (eHits.containsKey(player.getUniqueId())) {
            List<HitE> list = eHits.get(player.getUniqueId());
            Inventory inventory = Bukkit.createInventory(null, 27, "Expired Hits");
            list.forEach(i -> {
                inventory.addItem(i.getChest());
            });
            player.openInventory(inventory);
        }
        /*
        if (eHits.containsKey(player.getUniqueId())) {
            if (!HitTools.isInventoryEmpty(player.getInventory())) {
                HitE hit = eHits.remove(player.getUniqueId());
                player.getInventory().addItem(hit.getChest());
                Messages.success(player, "Your hit on " + hit.getBountyName() + " has expired.");
                Messages.success(player, "You have been gifted a chest, place it to receive your refund.");
            } else {
                Messages.fail(player, "You have an expired bounty!");
            }
        }

         */
    }
}
