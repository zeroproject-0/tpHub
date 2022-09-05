package dev.zeroproject.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.md_5.bungee.api.ChatColor;

public class MenuHandler implements Listener {

  @EventHandler
  public void onMenuClick(InventoryClickEvent event) {
    Player player = (Player) event.getWhoClicked();

    final String MENU_NAME = ChatColor.DARK_PURPLE + "Teleports Menu";

    if (event.getView().getTitle().equalsIgnoreCase(MENU_NAME)) {
      switch (event.getCurrentItem().getType()) {
        case GRASS_BLOCK:
          player.sendMessage("Location 1");
          player.closeInventory();
          break;
        case BARRIER:
          player.sendMessage("Close");
          player.closeInventory();
          break;

        default:
          break;
      }
    }
  }
}
