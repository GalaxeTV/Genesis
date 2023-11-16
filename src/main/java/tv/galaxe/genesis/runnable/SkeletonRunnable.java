package tv.galaxe.genesis.runnable;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public final class SkeletonRunnable implements Runnable {
	private Player player;

	public SkeletonRunnable(Player p) {
		player = p;
	}

	@Override
	public void run() {
		if (player.getGameMode().equals(GameMode.SURVIVAL) && player.getWorld().isDayTime()
				&& !(player.isInWaterOrRain()) && !(player.getLocation().getBlock().getLightFromSky() < 15)
				&& player.getEquipment().getHelmet() == null) {
			player.setFireTicks(60);
		}
	}
}
