package tv.galaxe.genesis;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class Genesis extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getLogger().log(Level.INFO, "Genesis has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getLogger().log(Level.INFO, "Genesis has been disabled!");
    }
}
