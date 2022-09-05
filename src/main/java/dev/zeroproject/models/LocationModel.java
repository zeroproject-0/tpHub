package dev.zeroproject.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationModel {
  private String player;
  private String name;
  private String world;
  private double x;
  private double y;
  private double z;
  private double yaw;
  private double pitch;

  public LocationModel(String player, String name, String world, double x, double y, double z, double yaw,
      double pitch) {
    this.player = player;
    this.name = name;
    this.world = world;
    this.x = x;
    this.y = y;
    this.z = z;
    this.yaw = yaw;
    this.pitch = pitch;
  }

  public LocationModel(Location location, String player, String name) {
    this.player = player;
    this.name = name;
    this.world = location.getWorld().getName();
    this.x = location.getX();
    this.y = location.getY();
    this.z = location.getZ();
    this.yaw = location.getYaw();
    this.pitch = location.getPitch();
  }

  public Location getLocation() {
    return new Location(Bukkit.getWorld(world), x, y, z, (float) yaw, (float) pitch);
  }

  public String getPlayer() {
    return player;
  }

  public void setPlayer(String player) {
    this.player = player;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getZ() {
    return z;
  }

  public void setZ(double z) {
    this.z = z;
  }

  public double getYaw() {
    return yaw;
  }

  public void setYaw(double yaw) {
    this.yaw = yaw;
  }

  public double getPitch() {
    return pitch;
  }

  public void setPitch(double pitch) {
    this.pitch = pitch;
  }

  @Override
  public String toString() {
    return "LocationModel [player=" + player + ", name=" + name + ", world=" + world + ", x=" + x + ", y=" + y + ", z="
        + z
        + ", yaw=" + yaw + ", pitch=" + pitch + "]";
  }
}
