package dev.zeroproject.utils;

import java.sql.Connection;

import dev.zeroproject.Plugin;

public abstract class Database {
  Plugin plugin;

  Connection connection;

  public Database(Plugin instance) {
    plugin = instance;
  }

  public abstract Connection getSQLConnection();

  public abstract void load();
}
