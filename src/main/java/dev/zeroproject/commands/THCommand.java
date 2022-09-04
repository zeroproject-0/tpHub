package dev.zeroproject.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class THCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if (!(sender instanceof Player)) {
      return false;
    }

    Player player = (Player) sender;

    if (args.length == 0) {
      sender.sendMessage("Usage: /th <action> <args>");
      return true;
    }

    switch (args[0].toLowerCase()) {
      case "help":
        player.sendMessage("Usage: /th add <name>");
        player.sendMessage("Usage: /th remove <name>");
        player.sendMessage("Usage: /th list");
        break;
      case "add":
        Location location = player.getLocation();
        player.sendMessage("Actual Coord: " + location.getX() + " " + location.getY() + " " + location.getZ());
        break;
      case "remove":
        player.sendMessage("Remove tp: " + args[1]);
        break;
      case "list":
        player.sendMessage("List: ");
        player.sendMessage("1. spawn");
        player.sendMessage("2. home");
        break;

      default:
        sender.sendMessage("Use for see all actions: /th help");
        break;
    }

    return true;
  }
}
