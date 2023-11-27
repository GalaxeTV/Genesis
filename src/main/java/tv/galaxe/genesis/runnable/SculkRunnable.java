package tv.galaxe.genesis.runnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tv.galaxe.genesis.Core;
import tv.galaxe.genesis.event.enforcer.Sculk;

public class SculkRunnable implements Runnable {
	private Player player;

	public SculkRunnable(Player p) {
		player = p;
	}

	@Override
	public void run() {
		ApplicableRegionSet set = Sculk.sculkQuery.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
		if (player.getGameMode().equals(GameMode.SURVIVAL)
				&& set.testState(WorldGuardPlugin.inst().wrapPlayer(player), Core.GENESIS_EFFECTS)
				&& player.getWorld().isDayTime() && !(player.getLocation().getBlock().getLightFromSky() < 15)
				&& player.getEquipment().getHelmet() == null) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60,
					Core.plugin.getConfig().getInt("classes.sculk.mining-fatigue-level")));
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60,
					Core.plugin.getConfig().getInt("classes.sculk.slowness-level")));
		}
	}
}
