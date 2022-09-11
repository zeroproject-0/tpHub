package dev.zeroproject.events;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    String currentTitle = event.getView().getTitle();

    if (currentTitle.startsWith(MENU_NAME)) {
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
        case WHITE_BANNER:
          Inventory menu = event.getClickedInventory();

          String bannerName = currentItemStack.getItemMeta().getDisplayName();

          String pageName = menu.getItem(menu.getSize() - 3).getItemMeta().getDisplayName();

          int pageNumber = Integer.parseInt(ChatColor.stripColor(pageName).split(" ")[1]) - 1;

          if (bannerName.equalsIgnoreCase(ChatColor.BLUE + "Next")) {

            pageNumber++;

            try {
              ArrayList<LocationModel> locations = db.getLocationPage(player.getUniqueId().toString(), pageNumber,
                  menu.getSize() - 4);

              if (locations.size() == 0) {
                player.sendMessage(ChatColor.RED + "No more locations");
                pageNumber--;
                return;
              }

              menu.clear();

              for (int i = 0; i < locations.size(); i++) {
                ItemStack item = new ItemStack(Material.COMPASS);

                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(locations.get(i).getName());
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                item.setItemMeta(meta);
                menu.setItem(i, item);

                ArrayList<String> locationLore = new ArrayList<String>();
                locationLore.add(ChatColor.GRAY + "Click to teleport to " + locations.get(i).getName());

                meta.setLore(locationLore);
                item.setItemMeta(meta);
              }
            } catch (SQLException e) {
              player.sendMessage(ChatColor.RED + "Error to get locations");
            }

            ItemStack close = new ItemStack(Material.BARRIER);
            ItemMeta closeMeta = close.getItemMeta();
            closeMeta.setDisplayName(ChatColor.RED + "Close");
            close.setItemMeta(closeMeta);

            ItemStack next = new ItemStack(Material.WHITE_BANNER);
            ItemMeta nextMeta = next.getItemMeta();
            nextMeta.setDisplayName(ChatColor.BLUE + "Next");
            next.setItemMeta(nextMeta);

            ItemStack previous = new ItemStack(Material.WHITE_BANNER);
            ItemMeta previousMeta = previous.getItemMeta();
            previousMeta.setDisplayName(ChatColor.BLUE + "Previous");
            previous.setItemMeta(previousMeta);

            ItemStack page = new ItemStack(Material.PAPER);
            ItemMeta pageMeta = page.getItemMeta();
            pageMeta.setDisplayName(ChatColor.BLUE + "Page " + (pageNumber + 1));
            page.setItemMeta(pageMeta);

            menu.setItem(menu.getSize() - 1, close);
            menu.setItem(menu.getSize() - 2, next);
            menu.setItem(menu.getSize() - 3, page);
            menu.setItem(menu.getSize() - 4, previous);

            player.openInventory(menu);

          } else if (bannerName.equalsIgnoreCase(ChatColor.BLUE + "Previous")) {
            if (pageNumber < 1)
              return;

            pageNumber--;

            try {
              ArrayList<LocationModel> locations = db.getLocationPage(player.getUniqueId().toString(), pageNumber,
                  menu.getSize() - 4);

              menu.clear();

              for (int i = 0; i < locations.size(); i++) {
                ItemStack item = new ItemStack(Material.COMPASS);

                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(locations.get(i).getName());
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                item.setItemMeta(meta);
                menu.setItem(i, item);

                ArrayList<String> locationLore = new ArrayList<String>();
                locationLore.add(ChatColor.GRAY + "Click to teleport to " + locations.get(i).getName());

                meta.setLore(locationLore);
                item.setItemMeta(meta);
              }
            } catch (SQLException e) {
              player.sendMessage(ChatColor.RED + "Error to get locations");
            }

            ItemStack close = new ItemStack(Material.BARRIER);
            ItemMeta closeMeta = close.getItemMeta();
            closeMeta.setDisplayName(ChatColor.RED + "Close");
            close.setItemMeta(closeMeta);

            ItemStack next = new ItemStack(Material.WHITE_BANNER);
            ItemMeta nextMeta = next.getItemMeta();
            nextMeta.setDisplayName(ChatColor.BLUE + "Next");
            next.setItemMeta(nextMeta);

            ItemStack previous = new ItemStack(Material.WHITE_BANNER);
            ItemMeta previousMeta = previous.getItemMeta();
            previousMeta.setDisplayName(ChatColor.BLUE + "Previous");
            previous.setItemMeta(previousMeta);

            ItemStack page = new ItemStack(Material.PAPER);
            ItemMeta pageMeta = page.getItemMeta();
            pageMeta.setDisplayName(ChatColor.BLUE + "Page " + (pageNumber + 1));
            page.setItemMeta(pageMeta);

            menu.setItem(menu.getSize() - 1, close);
            menu.setItem(menu.getSize() - 2, next);
            menu.setItem(menu.getSize() - 3, page);
            menu.setItem(menu.getSize() - 4, previous);

            player.openInventory(menu);
          }
          break;

        default:
          break;
      }

    }
  }
}
