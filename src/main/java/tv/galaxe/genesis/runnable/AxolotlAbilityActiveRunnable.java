package tv.galaxe.genesis.runnable;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import tv.galaxe.genesis.Core;
import tv.galaxe.genesis.event.enforcer.Axolotl;

public class AxolotlAbilityActiveRunnable implements Runnable {
	private Player player;

	public AxolotlAbilityActiveRunnable(Player p) {
		player = p;
	}

	@Override
	public void run() {
		// Check if out of ability points
		if (Axolotl.abilityMap.get(player) <= 1) {
			// Remove potion effects
			player.removePotionEffect(PotionEffectType.SLOW);
			player.removePotionEffect(PotionEffectType.REGENERATION);
			// Start recharging ability points
			Axolotl.abilityInactiveTaskMap.put(player, Core.plugin.getServer().getScheduler().runTaskTimer(Core.plugin,
					new AxolotlAbilityInactiveRunnable(player), 1, 1));
			// Temporarily store taskID to cancel runnable after removing from map.
			int taskID = Axolotl.abilityActiveTaskMap.get(player).getTaskId();
			Axolotl.abilityActiveTaskMap.remove(player);
			Core.plugin.getServer().getScheduler().cancelTask(taskID);
		}

		// Update charge and update bossbar
		Axolotl.abilityMap.put(player, Axolotl.abilityMap.get(player) - 1);
		Axolotl.abilityBar.get(player).progress((float) (Axolotl.abilityMap.get(player)
				/ Core.plugin.getConfig().getDouble("classes.axolotl.ability.charge")));
	}
}