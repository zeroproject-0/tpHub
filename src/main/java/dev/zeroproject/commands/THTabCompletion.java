package dev.zeroproject.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.zeroproject.models.LocationModel;
import dev.zeroproject.utils.SQLite;

public class THTabCompletion implements TabCompleter {
  private SQLite db;

  public THTabCompletion(SQLite db) {
    this.db = db;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    System.out.println(args.length);
    if (args.length == 1) {
      ArrayList<String> list = new ArrayList<String>();
      list.add("help");
      list.add("add");
      list.add("remove");
      list.add("list");
      list.add("show");
      return list;
    }

    if (args.length == 2) {
      Player player = (Player) sender;

      if (!(args[0].equalsIgnoreCase("add") ||
          args[0].equalsIgnoreCase("remove") ||
          args[0].equalsIgnoreCase("go")) ||
          args[0].equalsIgnoreCase("a") ||
          args[0].equalsIgnoreCase("r") ||
          args[0].equalsIgnoreCase("g")) {
        return null;
      }

      try {
        ArrayList<LocationModel> list = db.getLocations(player.getUniqueId().toString());

        ArrayList<String> names = new ArrayList<String>();
        for (LocationModel location : list) {
          names.add(location.getName());
        }

        return names;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return null;
  }

}
