package tv.galaxe.genesis.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerConnected implements Listener {
    @EventHandler
    public void onFirstJoin(PlayerLoginEvent event) {
        //TODO: Player join handling
    }
}
