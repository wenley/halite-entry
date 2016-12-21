import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GrowthStrategy {
  private final GameMap gameMap;
  private final int myID;
  private final int playerCount;

  // x coordinate -> y coordinate -> direction -> weight of recommendation [0.0, Infinity)
  private final Map<Integer, Map<Integer, MoveMap>> moveMap = new HashMap<>();

  public interface Heuristic {
    public void applyTo(GameMap gameMap, Location location, MoveMap moveMap);
  }

  public GrowthStrategy(GameMap gameMap, int myID, int playerCount) {
    this.gameMap = gameMap;
    this.myID = myID;
    this.playerCount = playerCount;

    computeRecommendedMoves();
  }

  public void computeRecommendedMoves() {
    PlayerAveragePositions playerAveragePositions = new PlayerAveragePositions(gameMap, playerCount);
    Set<Heuristic> heuristics = new HashSet<>();
    heuristics.add(AccumulateStrengthHeuristic.INSTANCE);
    heuristics.add(ConquerNeutralTerritoryHeuristic.INSTANCE);
    heuristics.add(new DiveIntoEnemyCenterHeuristic(myID, playerAveragePositions));
    heuristics.add(new MigrateStrengthToBorderHeuristic(gameMap, myID));

    for (int y = 0; y < gameMap.height; y++) {
      for (int x = 0; x < gameMap.width; x++) {
        Location myLocation = new Location(x, y);
        Site site = gameMap.getSite(myLocation);
        if (site.owner != myID) {
          continue;
        }

        MoveMap locationMap = moveMapFor(x, y);

        for (Heuristic heuristic : heuristics) {
          heuristic.applyTo(gameMap, myLocation, locationMap);
        }
      }
    }
  }

  public MoveMap moveMapFor(int x, int y) {
    if (!moveMap.containsKey(x)) {
      moveMap.put(x, new HashMap<>());
    }
    if (!moveMap.get(x).containsKey(y)) {
      moveMap.get(x).put(y, new MoveMap());
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
