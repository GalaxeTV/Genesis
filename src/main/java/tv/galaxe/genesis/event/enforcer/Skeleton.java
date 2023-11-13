package tv.galaxe.genesis.event.enforcer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import tv.galaxe.genesis.runnable.SkeletonRunnable;

// TODO: Find a way to store player's task ID to reference later when quitting the server.

public final class Skeleton implements Listener {
	@EventHandler
	public void onConnect(PlayerJoinEvent event) {
		if (event.getPlayer().hasPermission("genesis.genus.skeleton")) {
			event.getPlayer().getServer().getScheduler().runTaskTimer(
					event.getPlayer().getServer().getPluginManager().getPlugin("genesis"),
					new SkeletonRunnable((Player) event.getPlayer()), 0, 20);
		}
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		if (event.getPlayer().hasPermission("genesis.genus.skeleton")) {
			event.getPlayer().getServer().getScheduler().cancelTask(0);
		}
	}
}