package dev.zeroproject.commands.actions;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev.zeroproject.commands.ActionAbstract;
import dev.zeroproject.utils.SQLite;

public class RemoveAction extends ActionAbstract {

  @Override
  public String getName() {
    return "remove";
  }

  @Override
  public String getDescription() {
    return "Remove a location";
  }

  @Override
  public String getUsage() {
    return "/th remove <name>";
  }

  @Override
  public void run(String[] args, Player player, SQLite db) {
    String name = args[1];

    try {
      boolean result = db.removeLocation(name, player.getUniqueId().toString());
      String message = result ? (ChatColor.GREEN + "Location removed") : (ChatColor.RED + "Location not found");
      player.sendMessage(message);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
