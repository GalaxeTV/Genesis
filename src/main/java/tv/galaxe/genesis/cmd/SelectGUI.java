package tv.galaxe.genesis.cmd;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import java.net.MalformedURLException;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SelectGUI implements CommandExecutor {
	private static Gui selectGUI;
	private static Player player;

	public SelectGUI() throws MalformedURLException {
        /*
		PlayerProfile endermanProfile = Core.plugin.getServer().createProfile(UUID.randomUUID());
		endermanProfile.getTextures().setSkin(new URL(
				"http://textures.minecraft.net/texture/b3fbd454b599df593f57101bfca34e67d292a8861213d2202bb575da7fd091ac"),
				SkinModel.CLASSIC);
		ItemStack endermanSkull = ItemBuilder.from(Material.PLAYER_HEAD).name(Component.text("Enderman"))
				.lore(Component.text("Test")).build();
		((SkullMeta) endermanSkull.getItemMeta()).setPlayerProfile(endermanProfile);
        */

		selectGUI = Gui.gui().title(Component.text("Class Selection Menu")).rows(3).create();
		selectGUI.setDefaultClickAction(event -> {
			event.setCancelled(true);
		});
		selectGUI.getFiller().fill(new GuiItem(Material.BLACK_STAINED_GLASS_PANE));
		selectGUI.setItem(2, 5, new GuiItem(Material.NETHER_STAR));
		selectGUI.setItem(2, 1, new GuiItem(Material.SKELETON_SKULL));
		selectGUI.setItem(1, 2, new GuiItem(Material.PLAYER_HEAD));
		selectGUI.setItem(2, 3, new GuiItem(Material.PLAYER_HEAD));
		selectGUI.setItem(1, 4, new GuiItem(Material.PLAYER_HEAD));
		selectGUI.setItem(3, 6, new GuiItem(Material.PLAYER_HEAD));
		selectGUI.setItem(2, 7, new GuiItem(Material.PLAYER_HEAD));
		selectGUI.setItem(3, 8, new GuiItem(Material.PLAYER_HEAD));
		selectGUI.setItem(2, 9, new GuiItem(Material.PIGLIN_HEAD));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command must be used as a player!");
			return true;
		}
		player = (Player) sender;
		selectGUI.open(player);
		return true;
	}
}
