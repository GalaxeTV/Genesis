package tv.galaxe.genesis.runnable;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tv.galaxe.genesis.Core;

public class SculkRunnable implements Runnable {
	private Player player;

	public SculkRunnable(Player p) {
		player = p;
	}

	@Override
	public void run() {
		if (player.getGameMode().equals(GameMode.SURVIVAL) && player.getWorld().isDayTime()
				&& !(player.getLocation().getBlock().getLightFromSky() < 15)
				&& player.getEquipment().getHelmet() == null) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60,
					Core.plugin.getConfig().getInt("classes.sculk.mining-fatigue-level")));
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60,
					Core.plugin.getConfig().getInt("classes.sculk.slowness-level")));
		}
	}
}
