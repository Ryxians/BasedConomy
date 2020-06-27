package homebrew.basictrades.hit;

import homebrew.basictrades.BasicTrades;
import homebrew.basictrades.interfaces.HitI;
import homebrew.basictrades.tools.HitTools;
import homebrew.basictrades.tools.Messages;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class HitExpireTask extends BukkitRunnable {
    private HitI hit;
    protected long delay;

    public HitExpireTask(HitI hit, long delay) {
        if (hit instanceof HitO) {
            this.delay = delay;
            this.hit = hit;
            this.runTaskTimerAsynchronously(BasicTrades.instance, 0, 100);
        }
    }

    @Override
    public void run() {
        if (delay < 100) {
            //Cancel repeating task
            this.cancel();

            //Remove hit from hits
            BasicTrades.hits.remove(hit.getBountyUUID());
            Messages.success("Bounty on " + ChatColor.stripColor(hit.getBountyName()) + " has expired!");

            //Check if hit has an owner
            if (hit.getOwnerUUID() != null) {
                //Load hit to expired
                HitTools.loadExpiredHit(hit.getOwner(), hit.getBounty(), hit.getPrize());
            }
            HitTools.save();
        } else {
            delay -= 100;
        }
    }
}
