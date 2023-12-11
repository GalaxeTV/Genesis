package tv.galaxe.genesis.event.enforcer;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Human implements Listener {

	public static void newUser(Player player) {
		player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
	}

	public static void onConnect(PlayerJoinEvent event) {
		if (event.getPlayer().hasPermission("genesis.gui")) {
			event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
		}
	}
}
