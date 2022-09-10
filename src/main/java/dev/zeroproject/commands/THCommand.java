package dev.zeroproject.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.zeroproject.commands.actions.*;
import dev.zeroproject.models.LocationModel;
import dev.zeroproject.utils.SQLite;

public class THCommand implements CommandExecutor, TabCompleter {

  private Player player;

  private SQLite db;

  private Hashtable<String, ActionAbstract> actions;

  public THCommand(SQLite db) {
    this.db = db;
    this.actions = new Hashtable<String, ActionAbstract>();

    actions.put("remove", new RemoveAction());
    actions.put("add", new AddAction());
    actions.put("go", new GoAction());
    actions.put("list", new ListAction());
    actions.put("show", new ShowAction());
    actions.put("help", new HelpAction(this));
  }

  public Hashtable<String, ActionAbstract> getActions() {
    return actions;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if (!(sender instanceof Player)) {
      sender.sendMessage("You must be a player to execute this command!");
      return true;
    }

    this.player = (Player) sender;

    if (args.length == 0) {
      actions.get("help").run(null, player, null);
      return true;
    }

    if (args.length > 2) {
      sender.sendMessage("Too many arguments!");
      return true;
    }

    ActionAbstract action = actions.get(args[0]);

    if (action != null) {
      action.run(args, player, db);
    } else {
      sender.sendMessage("Invalid arguments! use /th help for more info");
    }

    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    if (args.length == 1)
      return new ArrayList<String>(actions.keySet());

    if (args.length == 2) {
      Player player = (Player) sender;

      if (!(args[0].equalsIgnoreCase("remove") ||
          args[0].equalsIgnoreCase("go"))) {
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
