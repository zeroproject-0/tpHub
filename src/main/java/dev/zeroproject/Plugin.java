package dev.zeroproject;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import dev.zeroproject.commands.THCommand;
import dev.zeroproject.events.MenuHandler;
import dev.zeroproject.utils.SQLite;

/*
 * tphub java plugin
 */
public class Plugin extends JavaPlugin {
  private static final Logger LOGGER = Logger.getLogger("TpHub");

  public void onEnable() {
    // Plugin startup logic
    LOGGER.info("tphub enabled");

    // Db
    SQLite db = new SQLite(this);
    db.load();

    // register event listener
    getServer().getPluginManager().registerEvents(new MenuHandler(db), this);

    // register command
    getCommand("th").setExecutor(new THCommand(db));

    // register tab completer
    getCommand("th").setTabCompleter(new THCommand(db));
  }

  public void onDisable() {
    // Plugin shutdown logic
    LOGGER.info("tphub disabled");
  }
}
