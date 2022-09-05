package dev.zeroproject.events;

import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import dev.zeroproject.models.LocationModel;
import dev.zeroproject.utils.SQLite;
import net.md_5.bungee.api.ChatColor;

public class MenuHandler implements Listener {

  private SQLite db;

  public MenuHandler(SQLite db) {
    this.db = db;
  }

  @EventHandler
  public void onMenuClick(InventoryClickEvent event) {
    Player player = (Player) event.getWhoClicked();

    final String MENU_NAME = ChatColor.DARK_PURPLE + "Teleports Menu";

    if (event.getView().getTitle().equalsIgnoreCase(MENU_NAME)) {
      switch (event.getCurrentItem().getType()) {
        case COMPASS:
          String name = event.getCurrentItem().getItemMeta().getDisplayName();

          try {
            LocationModel location = db.getLocation(name, player.getUniqueId().toString());
            if (location == null) {
              player.sendMessage(ChatColor.RED + "Location not found!");
              return;
            }

            player.teleport(location.getLocation());
            player.sendMessage("Teleported to: " + location.getName());
          } catch (SQLException e) {
            e.printStackTrace();
          }
          break;
        case BARRIER:
          break;

        default:
          break;
      }

      player.closeInventory();
    }
  }
}
