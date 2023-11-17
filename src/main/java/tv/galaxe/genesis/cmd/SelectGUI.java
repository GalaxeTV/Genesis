package tv.galaxe.genesis.cmd;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import tv.galaxe.genesis.Core;

public class SelectGUI implements CommandExecutor {
	private static Gui selectGUI;
	private static Player player;

	public SelectGUI() {
		selectGUI = Gui.gui().title(Component.text("Class Selection Menu")).rows(3).create();
		selectGUI.setDefaultClickAction(event -> {
			event.setCancelled(true);
		});
		selectGUI.getFiller().fill(new GuiItem(Material.BLACK_STAINED_GLASS_PANE));

		// Confirm Button
		selectGUI.setItem(2, 5, new GuiItem(Material.NETHER_STAR));

		// Skeleton
		selectGUI.setItem(2, 1, new GuiItem(Material.SKELETON_SKULL));

		// Enderman
		ItemStack enderman = createHead("7a59bb0a7a32965b3d90d8eafa899d1835f424509eadd4e6b709ada50b9cf");
		ItemMeta endermanMeta = enderman.getItemMeta();
		endermanMeta.displayName(Component.text("Enderman Class").decoration(TextDecoration.ITALIC, false));
		enderman.setItemMeta(endermanMeta);
		selectGUI.setItem(1, 2, new GuiItem(enderman, event -> {
		}));

		// Axolotl
		selectGUI.setItem(2, 3,
				new GuiItem(createHead("d704254139a0b1a926e7552482dd67679c6ae0dc8335c980dbd1c0d99634a708")));

		// Sculk
		selectGUI.setItem(1, 4,
				new GuiItem(createHead("bc9c84349742164a22971ee54516fff91d868da72cdcce62069db128c42154b2")));

		// Shulker
		selectGUI.setItem(3, 6,
				new GuiItem(createHead("1433a4b73273a64c8ab2830b0fff777a61a488c92f60f83bfb3e421f428a44")));

		// Strider
		selectGUI.setItem(2, 7,
				new GuiItem(createHead("a13cb566124aed3b5d86bfaf1d1b01f69526645622ed8510aa86a66d57096fe4")));

		// Phantom
		selectGUI.setItem(3, 8,
				new GuiItem(createHead("7e95153ec23284b283f00d19d29756f244313a061b70ac03b97d236ee57bd982")));

		// Piglin
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

	public static ItemStack createHead(String TextureID) {
		PlayerProfile headProfile = Core.plugin.getServer().createProfile(UUID.randomUUID());
		PlayerTextures headTextures = headProfile.getTextures();
		try {
			headTextures.setSkin(new URL("https://textures.minecraft.net/texture/" + TextureID));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		headProfile.setTextures(headTextures);
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = ((SkullMeta) skull.getItemMeta());
		meta.setPlayerProfile(headProfile);
		skull.setItemMeta(meta);
		return skull;
	}
}
