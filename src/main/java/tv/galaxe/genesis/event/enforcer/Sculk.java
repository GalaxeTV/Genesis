package tv.galaxe.genesis.event.enforcer;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import tv.galaxe.genesis.Core;
import tv.galaxe.genesis.runnable.SculkRunnable;

public class Sculk implements Listener {
    private static HashMap<Player, BukkitTask> taskMap = new HashMap<Player, BukkitTask>();

    @EventHandler
	public void onConnect(PlayerJoinEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.sculk")) {
			taskMap.put(event.getPlayer(), Core.plugin.getServer().getScheduler().runTaskTimer(Core.plugin,
					new SculkRunnable((Player) event.getPlayer()), 0, 20));
			event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH)
					.setBaseValue(Core.plugin.getConfig().getDouble("classes.sculk.max-health"));
		}
		event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE,
				PotionEffect.INFINITE_DURATION, 0, true, false, false));
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.sculk")
				&& event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			Core.plugin.getServer().getScheduler().cancelTask(taskMap.get(event.getPlayer()).getTaskId());
		}
	}
}
