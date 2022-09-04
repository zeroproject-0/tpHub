package dev.zeroproject;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * tphub java plugin
 */
public class Plugin extends JavaPlugin {
  private static final Logger LOGGER = Logger.getLogger("tphub");

  public void onEnable() {
    LOGGER.info("tphub enabled");
  }

  public void onDisable() {
    LOGGER.info("tphub disabled");
  }
}
