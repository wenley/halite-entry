import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class GrowthStrategy {
  private final GameMap gameMap;
  private final int myID;
  private final int playerCount;

  // x coordinate -> y coordinate -> direction -> weight of recommendation [0.0, Infinity)
  private final Map<Integer, Map<Integer, Map<Direction, Double>>> moveMap = new HashMap<>();

  public GrowthStrategy(GameMap gameMap, int myID, int playerCount) {
    this.gameMap = gameMap;
    this.myID = myID;
    this.playerCount = playerCount;

    computeRecommendedMoves();
  }

  public void computeRecommendedMoves() {
    PlayerAveragePositions playerAveragePositions = new PlayerAveragePositions(gameMap, playerCount);

    for (int y = 0; y < gameMap.height; y++) {
      for (int x = 0; x < gameMap.width; x++) {
        Location myLocation = new Location(x, y);
        Site site = gameMap.getSite(myLocation);

        // Bulk up weak sites
        // Vulnerable to invaders; want to move losers towards invaders
        if (site.strength < site.production * 5) {
          insertAdd(x, y, Direction.STILL, 1.0);
        }

        // Move towards neutral territory when you can take it over
        for (Direction direction : Direction.CARDINALS) {
          Site otherSite = gameMap.getSite(gameMap.getLocation(myLocation, direction));
          if (otherSite.owner == 0 && otherSite.strength < site.strength) {
            insertAdd(x, y, direction, 1.0);
          }
        }

        // When surrounded by enemies, move towards their center
        long numAdjacentEnemies = Arrays.stream(Direction.CARDINALS)
          .map(direction -> {
            Location twoAway = gameMap.getLocation(gameMap.getLocation(myLocation, direction), direction);
            return gameMap.getSite(twoAway);
          })
          .map(otherSite -> otherSite.owner)
          .filter(owner -> owner != 0 && owner != myID)
          .count();
        if (numAdjacentEnemies > 0) {
          Direction awayFromSelf = gameMap.moveTowards(playerAveragePositions.positionForOwner(myID).getLocation(), myLocation);
          Direction towardsEnemy = playerAveragePositions.positions().stream()
            .filter(entry -> entry.getKey() != myID)
            .map(Map.Entry::getValue)
            .map(PlayerAveragePositions.Position::getLocation)
            .min(myLocation.comparingByDistance())
            .map(location -> gameMap.moveTowards(myLocation, location))
            .orElse(awayFromSelf);

          insertAdd(x, y, towardsEnemy, numAdjacentEnemies);
        }
      }
    }
  }

  private void insertAdd(int x, int y, Direction direction, double value) {
    Map<Direction, Double> map = moveMapFor(x, y);
    if (!map.containsKey(direction)) {
      map.put(direction, 0.0);
    }
    map.put(direction, map.get(direction) + value);
  }

  public Map<Direction, Double> moveMapFor(int x, int y) {
    if (!moveMap.containsKey(x)) {
      moveMap.put(x, new HashMap<>());
    }
    if (!moveMap.get(x).containsKey(y)) {
      moveMap.get(x).put(y, new HashMap<>());
    }
    return moveMap.get(x).get(y);
  }

  public Direction recommendedDirection(int x, int y) {
    return moveMapFor(x, y).entrySet().stream()
      .max(Map.Entry.comparingByValue())
      .map(Map.Entry::getKey)
      .orElse(Direction.STILL);
  }
}
