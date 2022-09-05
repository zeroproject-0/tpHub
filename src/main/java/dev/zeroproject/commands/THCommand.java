package dev.zeroproject.commands;

import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.zeroproject.models.LocationModel;
import dev.zeroproject.utils.SQLite;
import net.md_5.bungee.api.ChatColor;

public class THCommand implements CommandExecutor {

  private Player player;

  private SQLite db;

  public THCommand(SQLite db) {
    this.db = db;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if (!(sender instanceof Player)) {
      sender.sendMessage("You must be a player to execute this command!");
      return true;
    }

    this.player = (Player) sender;

    if (args.length == 0) {
      sender.sendMessage("Usage: /th <action> <args>");
      return true;
    }

    if (args.length > 2) {
      sender.sendMessage("Too many arguments! use /th help for more info");
      return true;
    }

    if (args.length == 1) {
      if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))
        showHelp();
      else if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("l"))
        showList();
      else if (args[0].equalsIgnoreCase("show") || args[0].equalsIgnoreCase("s"))
        showTp();
      else {
        sender.sendMessage("Invalid arguments! use /th help for more info");
      }
      return true;
    }

    if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a"))
      addTp(args[1]);
    else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("r"))
      removeTp(args[1]);
    else if (args[0].equalsIgnoreCase("go") || args[0].equalsIgnoreCase("g"))
      goTp(args[1]);
    else {
      sender.sendMessage("Invalid arguments! use /th help for more info");
    }

    return true;
  }

  private void goTp(String name) {
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
  }

  private void showHelp() {
    player.sendMessage("Usage: /th add <name> or /th a <name>");
    player.sendMessage("Usage: /th remove <name> or /th r <name>");
    player.sendMessage("Usage: /th go <name> or /th g <name>");
    player.sendMessage("Usage: /th list or /th l");
    player.sendMessage("Usage: /th show or /th s");
  }

  private void removeTp(String name) {
    try {
      boolean result = db.removeLocation(name, player.getUniqueId().toString());
      String message = result ? (ChatColor.GREEN + "Location removed") : (ChatColor.RED + "Location not found");
      player.sendMessage(message);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void addTp(String name) {
    LocationModel locationModel = new LocationModel(player.getLocation(), player.getUniqueId().toString(), name);

    try {
      db.addLocation(locationModel);
      player.sendMessage(ChatColor.GREEN + "Location added!");

    } catch (SQLException e) {
      player.sendMessage(ChatColor.RED + "Error to save location please try with other name!");
    }
  }

  private void showList() {
    try {
      ArrayList<LocationModel> locations = db.getLocations(player.getUniqueId().toString());
      if (locations.size() == 0) {
        player.sendMessage(ChatColor.RED + "You don't have any location saved!");
        return;
      }

      player.sendMessage(ChatColor.GREEN + "Locations: ");

      for (int i = 0; i < locations.size(); i++) {
        player.sendMessage((i + 1) + ". " + locations.get(i).getName());
      }

    } catch (SQLException e) {
      player.sendMessage(ChatColor.RED + "Error to get locations");
    }
  }

  private void showTp() {
    Inventory mainMenu = Bukkit.createInventory(player, 9 * 6, ChatColor.DARK_PURPLE + "Teleports Menu");

    try {
      ArrayList<LocationModel> locations = db.getLocations(player.getUniqueId().toString());

      for (int i = 0; i < locations.size(); i++) {
        ItemStack item = new ItemStack(Material.COMPASS);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(locations.get(i).getName());
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

    mainMenu.setItem(53, close);

    player.openInventory(mainMenu);
  }
}
