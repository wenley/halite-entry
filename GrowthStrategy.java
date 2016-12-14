import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class GrowthStrategy {
  private final GameMap gameMap;

  // x coordinate -> y coordinate -> direction -> weight of recommendation [0.0, Infinity)
  private final Map<Integer, Map<Integer, Map<Direction, Double>>> moveMap = new HashMap<>();

  public GrowthStrategy(GameMap gameMap) {
    this.gameMap = gameMap;

    computeRecommendedMoves();
  }

  public void computeRecommendedMoves() {
    for (int y = 0; y < gameMap.height; y++) {
      for (int x = 0; x < gameMap.width; x++) {
        Location myLocation = new Location(x, y);
        Site site = gameMap.getSite(myLocation);

        // Bulk up weak sites
        // Vulnerable to invaders; want to move losers towards invaders
        if (site.strength < site.production * 5) {
          insert(x, y, Direction.STILL, 1.0);
        }

        for (Direction direction : Direction.CARDINALS) {
          Site otherSite = gameMap.getSite(gameMap.getLocation(myLocation, direction));
          if (otherSite.owner == 0 && otherSite.strength < site.strength) {
            insert(x, y, direction, 1.0);
          }
        }
      }
    }
  }

  public void insert(int x, int y, Direction direction, double value) {
    moveMapFor(x, y).put(direction, value);
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
