package tv.galaxe.genesis.runnable;

import org.bukkit.entity.Player;

public final class SkeletonRunnable implements Runnable {
	private static Player player;

	public SkeletonRunnable(Player p) {
		player = p;
	}

	@Override
	public void run() {
		if (player.getWorld().isDayTime() && !(player.isInWaterOrRain())
				&& !(player.getLocation().getBlock().getLightFromSky() < 15)
				&& player.getEquipment().getHelmet() == null) {
			player.setFireTicks(60);
		}
	}
}
