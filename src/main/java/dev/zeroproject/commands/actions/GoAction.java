package dev.zeroproject.commands.actions;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev.zeroproject.commands.ActionAbstract;
import dev.zeroproject.models.LocationModel;
import dev.zeroproject.utils.SQLite;

public class GoAction extends ActionAbstract {

  @Override
  public String getName() {
    return "go";
  }

  @Override
  public String getDescription() {
    return "Go to a location";
  }

  @Override
  public String getUsage() {
    return "/th go <name>";
  }

  @Override
  public void run(String[] args, Player player, SQLite db) {
    String name = args[1];

    try {
      LocationModel locationModel = db.getLocation(name, player.getUniqueId().toString());
      if (locationModel != null) {
        player.teleport(locationModel.getLocation());
        player.sendMessage(ChatColor.GREEN + "Teleported to " + name);
      } else {
        player.sendMessage(ChatColor.RED + "Location not found");
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
