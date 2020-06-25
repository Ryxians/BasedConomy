package homebrew.basictrades.hit;

import homebrew.basictrades.BasicTrades;
import homebrew.basictrades.interfaces.HitI;
import homebrew.basictrades.tools.HitTools;
import homebrew.basictrades.tools.Messages;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class HitExpireTask extends BukkitRunnable {
    private HitI hit;
    protected long delay;

    public HitExpireTask(HitI hit, long delay) {
        this.delay = delay;
        this.hit = hit;
        this.runTaskTimerAsynchronously(BasicTrades.instance, 0, 100);
    }

    @Override
    public void run() {
        if (delay < 100) {
            this.cancel();
            BasicTrades.hits.remove(hit.getBountyUUID());
            Messages.success("Bounty on " + ChatColor.stripColor(hit.getBountyName()) + " has expired!");
            if (hit.getOwnerUUID() != null) {
                HitE expiredHit = new HitE(hit.getOwner(), hit.getBounty(), hit.getPrize());
                BasicTrades.eHits.put(hit.getOwnerUUID(), expiredHit);
            }
            HitTools.save();
        } else {
            delay -= 100;
        }
    }
}
