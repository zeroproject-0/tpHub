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

    switch (args[0].toLowerCase()) {
      case "help":
        showHelp();
        break;
      case "add":
        addTp(args[1]);
        break;
      case "remove":
        player.sendMessage("Remove tp: " + args[1]);
        break;
      case "list":
        showList();
        break;
      case "show":
        showTp();
        break;
      case "go":
        // player.teleport(Location);
        player.sendMessage("Go to: " + args[1]);
        break;

      default:
        sender.sendMessage("Use for see all actions: /th help");
        break;
    }

    return true;
  }

  private void showHelp() {
    player.sendMessage("Usage: /th add <name>");
    player.sendMessage("Usage: /th remove <name>");
    player.sendMessage("Usage: /th go <name>");
    player.sendMessage("Usage: /th list");
  }

  private void addTp(String name) {
    LocationModel locationModel = new LocationModel(player.getLocation(), player.getUniqueId().toString(), name);

    player.sendMessage(locationModel.toString());

    try {
      db.addLocation(locationModel);
      player.sendMessage(ChatColor.GREEN + "Location added!");

    } catch (SQLException e) {
      player.sendMessage(ChatColor.RED + "Error to save location please try again!");
    }
  }

  private void showList() {
    try {
      ArrayList<LocationModel> locations = db.getLocations(player.getUniqueId().toString());
      player.sendMessage("List: ");

      for (int i = 0; i < locations.size(); i++) {
        player.sendMessage((i + 1) + ". " + locations.get(i).getName());
      }

    } catch (SQLException e) {
      player.sendMessage("Error to get locations");
    }
  }

  private void showTp() {
    Inventory mainMenu = Bukkit.createInventory(player, 9, ChatColor.DARK_PURPLE + "Teleports Menu");

    ItemStack location1 = new ItemStack(Material.GRASS_BLOCK);

    ItemMeta location1Meta = location1.getItemMeta();
    location1Meta.setDisplayName(ChatColor.GREEN + "Location 1");

    ArrayList<String> location1Lore = new ArrayList<String>();
    location1Lore.add(ChatColor.GRAY + "Click to teleport to Location 1");

    location1Meta.setLore(location1Lore);
    location1.setItemMeta(location1Meta);

    ItemStack close = new ItemStack(Material.BARRIER);
    ItemMeta closeMeta = close.getItemMeta();
    closeMeta.setDisplayName(ChatColor.RED + "Close");
    close.setItemMeta(closeMeta);

    mainMenu.setItem(0, location1);
    mainMenu.setItem(8, close);

    player.openInventory(mainMenu);
  }
}
