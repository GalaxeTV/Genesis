package tv.galaxe.genesis.event.enforcer;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitTask;
import tv.galaxe.genesis.Core;
import tv.galaxe.genesis.runnable.EndermanRunnable;

public final class Enderman implements Listener {
	private static Map<Player, BukkitTask> taskMap = new HashMap<Player, BukkitTask>();
	private static Map<Player, Instant> cooldownMap = new HashMap<Player, Instant>();

	@EventHandler
	public void onConnect(PlayerJoinEvent event) {
		if (event.getPlayer().hasPermission("genesis.genus.enderman")) {
			taskMap.put(event.getPlayer(), Core.plugin.getServer().getScheduler().runTaskTimer(Core.plugin,
					new EndermanRunnable((Player) event.getPlayer()), 0, 20));
			event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
		}
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		if (event.getPlayer().hasPermission("genesis.genus.enderman")) {
			Core.plugin.getServer().getScheduler().cancelTask(taskMap.get(event.getPlayer()).getTaskId());
		}
	}

	@EventHandler
	public void actionKey(PlayerSwapHandItemsEvent event) {
		if (event.getPlayer().hasPermission("genesis.genus.enderman")
				&& cooldownMap.getOrDefault(event.getPlayer(), Instant.now().minusSeconds(1)).isBefore(Instant.now())) {
			event.setCancelled(true);
			event.getPlayer().launchProjectile(EnderPearl.class);
			cooldownMap.put(event.getPlayer(), Instant.now().plusSeconds(30));
		} else {
			event.setCancelled(true);
			event.getPlayer()
					.sendActionBar(Component
							.text("You can use this ability in ")
                            .append(Component.text(String.format("%.1f",
													ChronoUnit.MILLIS.between(Instant.now(),
													cooldownMap.get(event.getPlayer())) / 1000.0)))
							.append(Component.text(" seconds!")));
		}
	}

	@EventHandler
	public void onEnderPearl(PlayerTeleportEvent event) {
		if (event.getCause() == TeleportCause.ENDER_PEARL) {
			event.setCancelled(true);
			event.getPlayer().teleport(event.getTo());
			event.getPlayer().getWorld().playSound(((Entity) event.getPlayer()), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F,
					1.0F);
		}
	}
}
