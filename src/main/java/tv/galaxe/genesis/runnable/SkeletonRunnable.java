package tv.galaxe.genesis.runnable;

import org.bukkit.entity.Player;

public class SkeletonRunnable implements Runnable {
	private Player player;

	public SkeletonRunnable(Player p) {
		this.player = p;
	}

	@Override
	public void run() {
		if (player.getWorld().isDayTime()
				&& !(player.getLocation().getBlock().getLightFromSky() < 15)
				&& player.getEquipment().getHelmet() == null) {
			player.setFireTicks(60);
		}
	}
}
