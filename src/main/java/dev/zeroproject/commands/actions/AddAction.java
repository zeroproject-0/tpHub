package dev.zeroproject.commands.actions;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev.zeroproject.commands.ActionAbstract;
import dev.zeroproject.models.LocationModel;
import dev.zeroproject.utils.SQLite;

public class AddAction extends ActionAbstract {

  @Override
  public String getName() {
    return "add";
  }

  @Override
  public String getDescription() {
    return "Add new a location";
  }

  @Override
  public String getUsage() {
    return "/th add <name>";
  }

  @Override
  public void run(String[] args, Player player, SQLite db) {
    String name = args[1];

    LocationModel locationModel = new LocationModel(player.getLocation(), player.getUniqueId().toString(), name);

    try {
      db.addLocation(locationModel);
      player.sendMessage(ChatColor.GREEN + "Location added!");

    } catch (SQLException e) {
      player.sendMessage(ChatColor.RED + "Error to save location please try with other name!");
    }
  }
}
