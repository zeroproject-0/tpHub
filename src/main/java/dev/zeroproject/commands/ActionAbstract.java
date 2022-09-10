package dev.zeroproject.commands;

import org.bukkit.entity.Player;

import dev.zeroproject.utils.SQLite;

public abstract class ActionAbstract {
  public abstract String getName();

  public abstract String getDescription();

  public abstract String getUsage();

  public abstract void run(String[] args, Player player, SQLite db);
}
