package tv.galaxe.genesis.event.enforcer;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import java.util.HashMap;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import tv.galaxe.genesis.Core;
import tv.galaxe.genesis.runnable.AxolotlAbilityActiveRunnable;
import tv.galaxe.genesis.runnable.AxolotlAbilityInactiveRunnable;
import tv.galaxe.genesis.runnable.AxolotlRunnable;

public class Axolotl implements Listener {
	private static HashMap<Player, BukkitTask> taskMap = new HashMap<Player, BukkitTask>();
	public static HashMap<Player, BukkitTask> abilityActiveTaskMap = new HashMap<Player, BukkitTask>();
	public static HashMap<Player, BukkitTask> abilityInactiveTaskMap = new HashMap<Player, BukkitTask>();
	public static HashMap<Player, Integer> abilityMap = new HashMap<Player, Integer>();
	public static HashMap<Player, BossBar> abilityBar = new HashMap<Player, BossBar>();
	public static RegionQuery axolotlQuery;

	public Axolotl() {
		axolotlQuery = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
	}

	public static void newUser(Player player) {
		taskMap.put(player,
				Core.plugin.getServer().getScheduler().runTaskTimer(Core.plugin, new AxolotlRunnable(player), 0, 20));
		player.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH)
				.setBaseValue(Core.plugin.getConfig().getDouble("classes.axolotl.max-health"));
		abilityMap.put(player, Core.plugin.getConfig().getInt("classes.axolotl.ability-charge"));
		player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, PotionEffect.INFINITE_DURATION, 0,
				true, false, false));
	}

	@EventHandler
	public void onConnect(PlayerJoinEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.axolotl")) {
			taskMap.put(event.getPlayer(), Core.plugin.getServer().getScheduler().runTaskTimer(Core.plugin,
					new AxolotlRunnable((Player) event.getPlayer()), 0, 20));
			event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH)
					.setBaseValue(Core.plugin.getConfig().getDouble("classes.axolotl.max-health"));
			abilityMap.put(event.getPlayer(), Core.plugin.getConfig().getInt("classes.axolotl.ability-charge"));
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE,
					PotionEffect.INFINITE_DURATION, 0, true, false, false));
		}
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.axolotl")) {
			Core.plugin.getServer().getScheduler().cancelTask(taskMap.get(event.getPlayer()).getTaskId());
		}
	}

	@EventHandler
	public void actionKey(PlayerSwapHandItemsEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.axolotl")
				&& event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			event.setCancelled(true);
			// Create bossbar if not initialized for player
			if (!abilityBar.containsKey(event.getPlayer())) {
				abilityBar.put(event.getPlayer(),
						BossBar.bossBar(Component.text("Resilient Charge").color(TextColor.color(236, 138, 195))
								.decorate(TextDecoration.BOLD), 1.0F, Color.PINK, Overlay.PROGRESS));
			}

			// Check if ability in use
			if (abilityActiveTaskMap.containsKey(event.getPlayer())) {
				// Remove potion effects
				event.getPlayer().removePotionEffect(PotionEffectType.SLOW);
				event.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
				// Cancel ability points reducer runnable
				Core.plugin.getServer().getScheduler()
						.cancelTask(abilityActiveTaskMap.get(event.getPlayer()).getTaskId());
				// Remove reducer runnable form taskmap
				abilityActiveTaskMap.remove(event.getPlayer());
				// Activate ability points increaser and add to taskmap
				abilityInactiveTaskMap.put(event.getPlayer(), Core.plugin.getServer().getScheduler().runTaskTimer(
						Core.plugin, new AxolotlAbilityInactiveRunnable((Player) event.getPlayer()), 0, 1));
			} else { // if ability is not in use (else)
				// Add potion effects
				event.getPlayer()
						.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, PotionEffect.INFINITE_DURATION,
								Core.plugin.getConfig().getInt("classes.axolotl.ability.slowness-level"), true, false,
								true));
				event.getPlayer()
						.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, PotionEffect.INFINITE_DURATION,
								Core.plugin.getConfig().getInt("classes.axolotl.ability.regen-level"), true, false,
								true));
				// If charge is increasing, stop that routine.
				// Otherwise show bossbar
				if (abilityInactiveTaskMap.containsKey(event.getPlayer())) {
					abilityInactiveTaskMap.get(event.getPlayer()).cancel();
					abilityInactiveTaskMap.remove(event.getPlayer());
				} else {
					event.getPlayer().showBossBar(abilityBar.get(event.getPlayer()));
				}
				// Activate ability charge decreaser and add to taskmap
				abilityActiveTaskMap.put(event.getPlayer(), Core.plugin.getServer().getScheduler()
						.runTaskTimer(Core.plugin, new AxolotlAbilityActiveRunnable((Player) event.getPlayer()), 0, 1));
			}
		}
	}

	@EventHandler
	public void onDamageAquatic(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof ElderGuardian || event.getEntity() instanceof Guardian
				|| event.getEntity() instanceof Drowned || event.getEntity() instanceof Guardian) {
			if (event.getDamager() instanceof AbstractArrow // Prevent errors by checking if the Damager is an
															// AbstractArrow
					&& ((Entity) ((AbstractArrow) event.getDamager()).getShooter())
							.hasPermission("genesis.classes.axolotl")) {
				event.setDamage(
						event.getDamage() + Core.plugin.getConfig().getDouble("classes.axolotl.aquatic-mob-buff"));
			}
			if (event.getDamager() instanceof Player && event.getDamager().hasPermission("genesis.classes.axolotl")) {
				event.setDamage(
						event.getDamage() + Core.plugin.getConfig().getDouble("classes.axolotl.aquatic-mob-buff"));
			}
		}
	}

	@EventHandler
	public void onDrowning(EntityDamageEvent event) {
		if (event.getEntity().hasPermission("genesis.classes.axolotl")
				&& event.getCause().equals(DamageCause.DROWNING)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.axolotl")) {
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE,
					PotionEffect.INFINITE_DURATION, 0, true, false, false));
		}
	}

	@EventHandler
	public void onDrinkMilk(PlayerItemConsumeEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.axolotl")
				&& event.getItem().getType() == Material.MILK_BUCKET) {
			event.setItem(new ItemStack(Material.BUCKET));
			event.getPlayer().clearActivePotionEffects();
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE,
					PotionEffect.INFINITE_DURATION, 0, true, false, false));
		}
	}
}
