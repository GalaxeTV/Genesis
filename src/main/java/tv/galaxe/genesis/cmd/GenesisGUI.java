package tv.galaxe.genesis.cmd;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class GenesisGUI implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)){
      sender.sendMessage("This command must be used as a player!");
      return false;
    } else {
      Player player = (Player)sender;

      // Create GUI w/open & close sounds
      Gui genesis = Gui.gui().title(Component.text("Genesis Help Menu")).rows(6).create();
      genesis.setOpenGuiAction(event -> { player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1); });
      genesis.setCloseGuiAction(event -> { player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1); });

      // Disable taking items
      genesis.setDefaultClickAction(event -> { event.setCancelled(true); });

      // Add filler
      genesis.getFiller().fill(Arrays.asList(
          ItemBuilder.from(Material.PINK_STAINED_GLASS_PANE).asGuiItem(),
          ItemBuilder.from(Material.PURPLE_STAINED_GLASS_PANE).asGuiItem()
      ));


    }
    return false;
  }
}
