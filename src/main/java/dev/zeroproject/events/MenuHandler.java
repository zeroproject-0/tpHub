package dev.zeroproject.events;

import java.sql.SQLException;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

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
      ItemStack currentItemStack = event.getCurrentItem();

      if (currentItemStack == null) {
        return;
      }

      event.setCancelled(true);

      switch (currentItemStack.getType()) {
        case COMPASS:
          String name = event.getCurrentItem().getItemMeta().getDisplayName();

          Set<ItemFlag> flags = currentItemStack.getItemMeta().getItemFlags();

          if (!flags.contains(ItemFlag.HIDE_UNBREAKABLE))
            return;

          try {
            LocationModel location = db.getLocation(name, player.getUniqueId().toString());

            player.teleport(location.getLocation());
            player.sendMessage("Teleported to: " + location.getName());
            player.closeInventory();
          } catch (SQLException e) {
            e.printStackTrace();
          }
          break;
        case BARRIER:
          player.closeInventory();
          break;

        default:
          break;
      }

    }
  }
}
