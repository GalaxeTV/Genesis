package tv.galaxe.genesis.event.enforcer;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import java.util.EnumSet;
import java.util.HashMap;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import tv.galaxe.genesis.Core;
import tv.galaxe.genesis.runnable.ShulkerRunnable;

public final class Shulker implements Listener {
	private static HashMap<Player, BukkitTask> taskMap = new HashMap<Player, BukkitTask>();

	EnumSet<Material> heavyArmor = EnumSet.of(Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS,
			Material.IRON_BOOTS, Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS,
			Material.GOLDEN_BOOTS, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS,
			Material.DIAMOND_BOOTS, Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE,
			Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS);

	@EventHandler
	public void onConnect(PlayerJoinEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.shulker")) {
			taskMap.put(event.getPlayer(), Core.plugin.getServer().getScheduler().runTaskTimer(Core.plugin,
					new ShulkerRunnable((Player) event.getPlayer()), 0, 20));
			event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH)
					.setBaseValue(Core.plugin.getConfig().getDouble("classes.shulker.max-health"));
			event.getPlayer().getAttribute(Attribute.GENERIC_ARMOR)
					.setBaseValue(Core.plugin.getConfig().getDouble("classes.shulker.natural-armor"));
		}
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.shulker")
				&& event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			Core.plugin.getServer().getScheduler().cancelTask(taskMap.get(event.getPlayer()).getTaskId());
		}
	}

	@EventHandler
	public void onArmorEquip(PlayerArmorChangeEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.shulker")
				&& event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			ItemStack[] armorContents = event.getPlayer().getInventory().getArmorContents();
			boolean wearingHeavyArmor = false;
			for (int i = 0; i < armorContents.length; i++) {
				try {
					if (heavyArmor.contains(armorContents[i].getType())) {
						wearingHeavyArmor = true;
					}
				} catch (Exception e) {
					// Fail silently
				}
			}
			if (wearingHeavyArmor) {
				event.getPlayer()
						.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, PotionEffect.INFINITE_DURATION,
								Core.plugin.getConfig().getInt("classes.shulker.heavy-armor-slowness"), false, false,
								true));
				event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_ANVIL_LAND, 0.5F, 0.8F);
			} else {
				event.getPlayer().removePotionEffect(PotionEffectType.SLOW);
			}
		}
	}
}