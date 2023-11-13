package tv.galaxe.genesis.runnable;

import org.bukkit.entity.Player;

public class SkeletonRunnable implements Runnable {
	private Player player;

	public SkeletonRunnable(Player p) {
		this.player = p;
	}

	@Override
	public void run() {
		if (player.getWorld().isDayTime() && player.getInventory().getHelmet() == null
				&& !(player.getLocation().getBlock().getRelative(0, 1, 0).getLightFromSky() < 15)) {
			player.setFireTicks(60);
		}
	}
}
