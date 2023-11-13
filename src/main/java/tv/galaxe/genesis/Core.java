package tv.galaxe.genesis;

import org.bukkit.plugin.java.JavaPlugin;
import tv.galaxe.genesis.cmd.GenesisGUI;

public final class Core extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getConsoleSender().sendMessage("Genesis has been enabled!");
        saveDefaultConfig();

        // Commands
        getCommand("genesis").setExecutor(new GenesisGUI());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getConsoleSender().sendMessage("Genesis has been disabled!");
    }
}
