import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class PlayerAveragePositions {
  private final GameMap gameMap;
  private final int playerCount;
  private final Map<Integer, Position> playerPositions = new HashMap<>();

  static class Position {
    final Location location;
    final long mass;

    private Position(Location location, long mass) {
      this.location = location;
      this.mass = mass;
    }
  }

  PlayerAveragePositions(GameMap gameMap, int playerCount) {
    this.gameMap = gameMap;
    this.playerCount = playerCount;

    computeSinkLocations();
  }

  public Position positionForOwner(int owner) {
    if (owner > playerCount) {
      throw new RuntimeException(String.format("%d vs. player count %d", owner, playerCount));
    }

    return playerPositions.get(owner);
  }

  /** On the torus, where is the strength-weighted average position of each player? */
  private void computeSinkLocations() {
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
      long avgX = weightedX[owner] / totalMass[owner];
      long avgY = weightedY[owner] / totalMass[owner];
      Location avgLocation = new Location((int) avgX, (int) avgY);
      playerPositions.put(owner, new Position(avgLocation, totalMass[owner]));
    }
  }
}