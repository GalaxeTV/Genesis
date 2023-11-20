package tv.galaxe.genesis.event.enforcer;

import java.util.HashMap;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import tv.galaxe.genesis.Core;
import tv.galaxe.genesis.runnable.SkeletonRunnable;

public final class Skeleton implements Listener {
	private static HashMap<Player, BukkitTask> taskMap = new HashMap<Player, BukkitTask>();

	@EventHandler
	public void onConnect(PlayerJoinEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.skeleton")) {
			taskMap.put(event.getPlayer(), Core.plugin.getServer().getScheduler().runTaskTimer(Core.plugin,
					new SkeletonRunnable((Player) event.getPlayer()), 0, 20));
			event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH)
					.setBaseValue(Core.plugin.getConfig().getDouble("classes.skeleton.max-health"));
		}
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.skeleton")
				&& event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			Core.plugin.getServer().getScheduler().cancelTask(taskMap.get(event.getPlayer()).getTaskId());
		}
	}

	@EventHandler
	public void onDrinkMilk(PlayerItemConsumeEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.skeleton")
				&& event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)
				&& event.getItem().getType() == Material.MILK_BUCKET) {
			event.setItem(new ItemStack(Material.BUCKET));
			event.getPlayer().clearActivePotionEffects();
			event.getPlayer()
					.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,
							Core.plugin.getConfig().getInt("classes.skeleton.milk-buff.duration-ticks"),
							Core.plugin.getConfig().getInt("classes.skeleton.milk-buff.amplifier")));
		}
	}

	@EventHandler
	public void onShootBowDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof AbstractArrow // Prevent errors by checking if the Damager is an AbstractArrow
				&& ((Entity) ((AbstractArrow) event.getDamager()).getShooter())
						.hasPermission("genesis.classes.skeleton")
				&& ((Player) ((AbstractArrow) event.getDamager()).getShooter()).getGameMode().equals(GameMode.SURVIVAL)
				&& !(event.getDamager() instanceof Trident)) {
			event.setDamage(event.getDamage() + Core.plugin.getConfig().getDouble("classes.skeleton.arrow-buff"));
		}
	}

	@EventHandler
	public void onUndeadMobTarget(EntityTargetLivingEntityEvent event) {
		if (event.getTarget() != null && event.getTarget().hasPermission("genesis.classes.skeleton")
				&& event.getEntity() instanceof AbstractSkeleton) {
			event.setCancelled(true);
		}
	}
}