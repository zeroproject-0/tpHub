package dev.zeroproject;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import dev.zeroproject.commands.THCommand;
import dev.zeroproject.listeners.PlayerListener;

/*
 * tphub java plugin
 */
public class Plugin extends JavaPlugin {
  private static final Logger LOGGER = Logger.getLogger("TpHub");

  public void onEnable() {
    // Plugin startup logic
    LOGGER.info("tphub enabled");

    // register event listener
    getServer().getPluginManager().registerEvents(new PlayerListener(), this);

    // register command
    getCommand("th").setExecutor(new THCommand());
  }

  public void onDisable() {
    // Plugin shutdown logic
    LOGGER.info("tphub disabled");
  }
}
