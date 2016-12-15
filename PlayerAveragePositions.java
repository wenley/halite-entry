import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class PlayerAveragePositions {
  private final GameMap gameMap;
  private final int playerCount;
  private final Map<Integer, Position> playerPositions = new HashMap<>();

  static class Position {
    private final Location location;
    private final long mass;

    private Position(Location location, long mass) {
      this.location = location;
      this.mass = mass;
    }

    Location getLocation() {
      return location;
    }

    long getMass() {
      return mass;
    }
  }

  PlayerAveragePositions(GameMap gameMap, int playerCount) {
    this.gameMap = gameMap;
    this.playerCount = playerCount;

    computeSinkLocations();
  }

  public Set<Map.Entry<Integer, Position>> positions() {
    return playerPositions.entrySet();
  }

  public Position positionForOwner(int owner) {
    if (owner > playerCount) {
      return SoftErrors.throwOrDefault(new Position(new Location(0, 0), 0),
          new RuntimeException(String.format("%d vs. player count %d", owner, playerCount)));
    }

    return playerPositions.get(owner);
  }

  /** On the torus, where is the strength-weighted average position of each player? */
  // TODO: How to make this torus-sensitive?
  private void computeSinkLocations() {
    Logger.log("Computing player locations...");
    long[] weightedX = new long[playerCount + 1];
    long[] weightedY = new long[playerCount + 1];
    long[] totalMass = new long[playerCount + 1];

    for (int y = 0; y < gameMap.height; y++) {
      for (int x = 0; x < gameMap.width; x++) {
        Site site = gameMap.getSite(new Location(x, y));
        int mass = site.strength + site.production;

        weightedX[site.owner] += x * mass;
        weightedY[site.owner] += y * mass;
        totalMass[site.owner] += mass;
      }
    }

    for (int owner = 1; owner <= playerCount; owner++) {
      long avgX = weightedX[owner] / (totalMass[owner] + 1);
      long avgY = weightedY[owner] / (totalMass[owner] + 1);
      Location avgLocation = new Location((int) avgX, (int) avgY);
      playerPositions.put(owner, new Position(avgLocation, totalMass[owner] + 1));
    }
  }
}
