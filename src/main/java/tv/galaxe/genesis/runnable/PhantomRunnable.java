package tv.galaxe.genesis.runnable;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import tv.galaxe.genesis.Core;

public class PhantomRunnable implements Runnable {
	private Player player;

	public PhantomRunnable(Player p) {
		player = p;
	}

	@Override
	public void run() {
		if (player.getGameMode().equals(GameMode.SURVIVAL) && player.getWorld().isDayTime()
				&& !(player.isInWaterOrRain()) && !(player.getLocation().getBlock().getLightFromSky() < 15)
				&& player.getEquipment().getHelmet() == null) {
			player.setFireTicks(Core.plugin.getConfig().getInt("classes.phantom.daylight-fire-ticks"));
		}
	}
}
