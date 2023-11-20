package tv.galaxe.genesis.runnable;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import tv.galaxe.genesis.Core;
import tv.galaxe.genesis.event.enforcer.Axolotl;

public class AxolotlAbilityInactiveRunnable implements Runnable {
	private Player player;

	public AxolotlAbilityInactiveRunnable(Player p) {
		player = p;
	}

	@Override
	public void run() {
		if (Axolotl.abilityMap.get(player) >= Core.plugin.getConfig().getInt("classes.axolotl.ability.cooldown") - 1) {
			int taskID = Axolotl.abilityInactiveTaskMap.get(player).getTaskId();
			Axolotl.abilityInactiveTaskMap.remove(player);
			Core.plugin.getServer().getScheduler().cancelTask(taskID);
		}
		Axolotl.abilityMap.put(player, Axolotl.abilityMap.get(player) + 1);
		player.sendActionBar(Component.text("Ability Charge: " + Axolotl.abilityMap.get(player)));
	}
}