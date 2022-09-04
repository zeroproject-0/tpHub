package dev.zeroproject.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    player.sendMessage("Welcome to the server! " + player.getName());
  }

  @EventHandler
  public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();
    player.sendMessage("actually u send a message " + player.getName());
  }
}
