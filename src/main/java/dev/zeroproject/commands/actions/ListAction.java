package dev.zeroproject.commands.actions;

import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev.zeroproject.commands.ActionAbstract;
import dev.zeroproject.models.LocationModel;
import dev.zeroproject.utils.SQLite;

public class ListAction extends ActionAbstract {

  @Override
  public String getName() {
    return "list";
  }

  @Override
  public String getDescription() {
    return "List all locations";
  }

  @Override
  public String getUsage() {
    return "/th list";
  }

  @Override
  public void run(String[] args, Player player, SQLite db) {
    try {
      ArrayList<LocationModel> locations = db.getAllLocations(player.getUniqueId().toString());
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
}
