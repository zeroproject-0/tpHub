package dev.zeroproject.commands.actions;

import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.zeroproject.commands.ActionAbstract;
import dev.zeroproject.models.LocationModel;
import dev.zeroproject.utils.SQLite;

public class ShowAction extends ActionAbstract {

  @Override
  public String getName() {
    return "show";
  }

  @Override
  public String getDescription() {
    return "Show a menu with all locations";
  }

  @Override
  public String getUsage() {
    return "/th show";
  }

  @Override
  public void run(String[] args, Player player, SQLite db) {
    int inventorySize = 9 * 3;

    Inventory mainMenu = Bukkit.createInventory(player, inventorySize, ChatColor.DARK_PURPLE + "Teleports Menu");

    try {
      ArrayList<LocationModel> locations = db.getLocationPage(player.getUniqueId().toString(), 0, inventorySize - 4);

      for (int i = 0; i < locations.size(); i++) {
        ItemStack item = new ItemStack(Material.COMPASS);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(locations.get(i).getName());
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        mainMenu.setItem(i, item);

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
    pageMeta.setDisplayName(ChatColor.BLUE + "Page 1");
    page.setItemMeta(pageMeta);

    mainMenu.setItem(mainMenu.getSize() - 1, close);
    mainMenu.setItem(mainMenu.getSize() - 2, next);
    mainMenu.setItem(mainMenu.getSize() - 3, page);
    mainMenu.setItem(mainMenu.getSize() - 4, previous);

    player.openInventory(mainMenu);
  }
}
