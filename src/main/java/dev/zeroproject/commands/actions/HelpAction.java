package dev.zeroproject.commands.actions;

import java.util.Hashtable;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev.zeroproject.commands.ActionAbstract;
import dev.zeroproject.commands.THCommand;
import dev.zeroproject.utils.SQLite;

public class HelpAction extends ActionAbstract {
  private THCommand thCommand;

  public HelpAction(THCommand thCommand) {
    this.thCommand = thCommand;
  }

  @Override
  public String getName() {
    return "help";
  }

  @Override
  public String getDescription() {
    return "Show help";
  }

  @Override
  public String getUsage() {
    return "/th help";
  }

  @Override
  public void run(String[] args, Player player, SQLite db) {
    Hashtable<String, ActionAbstract> actions = thCommand.getActions();

    player.sendMessage(ChatColor.GREEN + "Teleport Hub Help: ");
    actions.forEach((key, action) -> {
      player.sendMessage(ChatColor.GREEN + action.getUsage() + ": " + ChatColor.GRAY + action.getDescription());
    });
  }
}
