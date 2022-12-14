package dev.zeroproject.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;

import dev.zeroproject.Plugin;
import dev.zeroproject.models.LocationModel;

public class SQLite extends Database {
  String dbname;

  public SQLite(Plugin instance) {
    super(instance);
    dbname = plugin.getConfig().getString("SQLite.Filename", "THDatabase");
  }

  public final String TABLE_LOCATIONS = "CREATE TABLE IF NOT EXISTS Locations (" +
      "`player` VARCHAR(36) NOT NULL," +
      "`name` varchar(255) NOT NULL," +
      "`world` varchar(20) NOT NULL," +
      "`x` double NOT NULL," +
      "`y` double NOT NULL," +
      "`z` double NOT NULL," +
      "`yaw` double NOT NULL," +
      "`pitch` double NOT NULL," +
      "`date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP," +
      "PRIMARY KEY (`player`, `name`)" +
      ");";

  public Connection getSQLConnection() {
    if (!plugin.getDataFolder().exists()) {
      plugin.getDataFolder().mkdir();
    }

    File dataFolder = new File(plugin.getDataFolder(), dbname + ".db");

    if (!dataFolder.exists()) {
      try {
        dataFolder.createNewFile();
      } catch (IOException e) {
        plugin.getLogger().log(Level.SEVERE, "File write error: " + dbname + ".db");
      }
    }

    try {
      if (connection != null && !connection.isClosed())
        return connection;

      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);

      return connection;
    } catch (SQLException ex) {
      plugin.getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
    } catch (ClassNotFoundException ex) {
      plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
    }
    return null;
  }

  public void load() {
    connection = getSQLConnection();
    try {
      Statement s = connection.createStatement();
      s.executeUpdate(TABLE_LOCATIONS);
      s.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public LocationModel getLocation(String name, String playerId) throws SQLException {
    LocationModel location = null;
    try {
      ResultSet result = getSQLConnection().createStatement()
          .executeQuery(
              "SELECT * FROM Locations WHERE name = '" + name + "' AND player = '" + playerId + "' ORDER BY date;");
      while (result.next()) {
        location = new LocationModel(result);
      }
      result.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException();
    }
    return location;
  }

  public void addLocation(LocationModel locationModel) throws SQLException {
    Connection conn = null;
    Statement s = null;

    try {
      conn = getSQLConnection();
      s = conn.createStatement();
      s.execute(
          "INSERT INTO Locations (player, name, world, x, y, z, yaw, pitch, date) VALUES ('"
              + locationModel.getPlayer() + "', '"
              + locationModel.getName() + "', '"
              + locationModel.getWorld() + "', '"
              + locationModel.getX() + "', '"
              + locationModel.getY() + "', '"
              + locationModel.getZ() + "', '"
              + locationModel.getYaw() + "', '"
              + locationModel.getPitch() + "', "
              + "CURRENT_TIMESTAMP" + ");");

    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException();
    } finally {
      try {
        if (s != null)
          s.close();
        if (conn != null)
          conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public boolean removeLocation(String name, String playerId) throws SQLException {
    Connection conn = null;
    Statement s = null;
    boolean result = false;

    try {
      conn = getSQLConnection();
      s = conn.createStatement();
      int r = s.executeUpdate(
          "DELETE FROM Locations WHERE name = '" + name + "' AND player = '" + playerId + "';");

      result = r > 0;

    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException();
    } finally {
      try {
        if (s != null)
          s.close();
        if (conn != null)
          conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return result;
  }

  public ArrayList<LocationModel> getAllLocations(String playerId) throws SQLException {
    ArrayList<LocationModel> tps = null;

    Connection conn = null;
    Statement s = null;

    try {
      tps = new ArrayList<LocationModel>();

      conn = getSQLConnection();
      s = conn.createStatement();

      ResultSet rs = s.executeQuery("SELECT * FROM Locations WHERE player='" + playerId + "' ORDER BY date;");

      while (rs.next()) {
        LocationModel tp = new LocationModel(rs);

        tps.add(tp);
      }
      rs.close();

    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException();
    } finally {
      try {
        if (s != null)
          s.close();
        if (conn != null)
          conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return tps;
  }

  public ArrayList<LocationModel> getLocationPage(String playerId, int pageNumber, int limit) throws SQLException {
    ArrayList<LocationModel> tps = null;

    Connection conn = null;
    Statement s = null;

    try {
      tps = new ArrayList<LocationModel>();

      conn = getSQLConnection();
      s = conn.createStatement();

      ResultSet rs = s
          .executeQuery("SELECT * FROM Locations WHERE player='" + playerId + "' ORDER BY date LIMIT " + limit
              + " OFFSET " + limit * pageNumber + ";");

      while (rs.next()) {
        LocationModel tp = new LocationModel(rs);

        tps.add(tp);
      }
      rs.close();

    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException();
    } finally {
      try {
        if (s != null)
          s.close();
        if (conn != null)
          conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return tps;
  }
}
