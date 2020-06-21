package homebrew.basictrades.commands;

import homebrew.basictrades.BasicTrades;
import homebrew.basictrades.HitO;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.UUID;

public class HitC implements CommandExecutor {
    BasicTrades plugin = BasicTrades.instance;
    Map<UUID, HitO> hits = plugin.hits;
    Map<Inventory, HitO> sHits = plugin.sHits;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player cmdSender = (Player)sender;
            switch (args.length) {
                case 0:
                    //When no player is specified
                    BasicTrades.fail(sender, "Please specify a player.");
                    break;
                case 1:
                    //When one person is specified
                    if (checkCommand(args, cmdSender)) {
                        placesHit(cmdSender, Bukkit.getOfflinePlayer(args[0]), false);
                    }

                    break;
                case 2:
                    //cmdSender.sendMessage(String.valueOf(args[1] == "a"));
                    if (cmdSender.hasPermission("BasedHits.anonymous.hit") && args[1].equals("a")) {
                        if (checkCommand(args, cmdSender)) {
                            placesHit(cmdSender, Bukkit.getOfflinePlayer(args[0]), true);
                            BasicTrades.success(cmdSender, "Anonymous Hit open, " + ChatColor.RED + "you will not be able to reclaim these items!");
                            break;
                        }
                    }
                default:
                    //when more than one person is specified
                    BasicTrades.fail(sender, "Excuse me, one hit at a time.");
                    break;
            }
        } else {
            BasicTrades.fail(sender, "This is a player command.");
        }
        return true;
    }

    private boolean checkCommand(String[] args, Player cmdSender) {
        boolean rc;
        if (Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()) {
            OfflinePlayer hitUsr = Bukkit.getOfflinePlayer(args[0]);
            if (!hits.containsKey(hitUsr.getUniqueId())) {
                rc = true;
            } else {
                BasicTrades.fail(cmdSender, "That player already has a hit >>:(");
                rc = false;
            }
        } else {
            BasicTrades.fail(cmdSender, "That is not a player >:(");
            rc = false;
        }
        return rc;
    }

    private void placesHit(Player cmdSender, OfflinePlayer hitUsr, boolean isAnonymous) {
        Inventory temp = cmdSender.openInventory(Bukkit.createInventory(cmdSender, 27, "Hit Price")).getTopInventory();
        if (isAnonymous) {
            cmdSender = null;
        }
        HitO hit = new HitO(cmdSender, hitUsr, temp);
        sHits.put(temp, hit);
    }
}
