package dev.zeroproject.utils;

import java.util.logging.Level;

import dev.zeroproject.Plugin;

public class Error {
  public static void execute(Plugin plugin, Exception ex) {
    plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
  }

  public static void close(Plugin plugin, Exception ex) {
    plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
  }
}
