package homebrew.basictrades.hit;

import homebrew.basictrades.BasicTrades;
import homebrew.basictrades.tools.HitTools;
import homebrew.basictrades.tools.Messages;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class HitExpireTask extends BukkitRunnable {
    private HitO hit;
    protected long delay;

    public HitExpireTask(HitO hit, long delay) {
        this.delay = delay;
        this.hit = hit;
        this.runTaskTimerAsynchronously(BasicTrades.instance, 0, 100);
    }

    @Override
    public void run() {
        if (delay < 100) {
            this.cancel();
            BasicTrades.hits.remove(hit.bounty);
            Messages.success("Bounty on " + ChatColor.stripColor(hit.getBountyName()) + " has expired!");
            if (hit.owner != null) {
                BasicTrades.eHits.put(hit.owner, hit);
            }
            HitTools.save();
        } else {
            delay -= 100;
        }
    }
}
