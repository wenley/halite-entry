import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

public class MigrateStrengthToBorderHeuristic implements GrowthStrategy.Heuristic {
  // Sync with AccumulateStrengthHeuristic ratio??
  private static final int MIN_STRENGTH_RATIO = 5;

  // Must be less than Accumulate heuristic
  private static final double WEIGHT = 0.9;

  private final GameMap gameMap;
  private final int myID;

  // vs. 2D array: Lower memory footprint, but maybe slower?
  private final Map<Location, Direction> toClosestBorder = new HashMap<>();

  public MigrateStrengthToBorderHeuristic(GameMap gameMap, int myID) {
    this.gameMap = gameMap;
    this.myID = myID;

    computePathToBorder();
  }

  private void computePathToBorder() {
    Set<Location> frontier = findMyFrontier(findFirstOtherLocation(gameMap));

    int[][] distanceToBorder = new int[gameMap.width][gameMap.height];
    fillDistanceToBorder(distanceToBorder, frontier, 1);

    determineClosestBorder(distanceToBorder);
  }

  private void determineClosestBorder(int[][] distanceToBorder) {
    for (Location location : gameMap.locations()) {
      if (gameMap.getSite(location).owner != myID) {
        continue;
      }

      Map<Direction, Integer> distances = new HashMap<>();

      for (Direction direction : Direction.CARDINALS) {
        Location otherLocation = gameMap.getLocation(location, direction);
        distances.put(direction, distanceToBorder[otherLocation.x][otherLocation.y]);
      }

      toClosestBorder.put(location, distances.entrySet().stream()
        .min(Map.Entry.comparingByValue())
        .get().getKey());
    }
  }

  private Location findFirstOtherLocation(GameMap gameMap) {
    for (Location location : gameMap.locations()) {
      Site site = gameMap.getSite(location);
      if (site.owner != myID) {
        return location;
      }
    }

    return SoftErrors.throwOrDefault(new Location(0, 0), new RuntimeException("Couldn't find un-owned space in game map"));
  }

  private void fillDistanceToBorder(int[][] distanceToBorder, Set<Location> frontier, int distance) {
    // Got passed an empty frontier
    if (frontier.size() == 0) {
      return;
    }

    for (Location location : frontier) {
      distanceToBorder[location.x][location.y] = distance;
    }

    Set<Location> nextFrontier = new HashSet<>();
    for (Location location : frontier) {
      for (Direction direction : Direction.CARDINALS) {
        Location otherLocation = gameMap.getLocation(location, direction);
        if (distanceToBorder[otherLocation.x][otherLocation.y] != 0) {
          continue;
        }

        Site site = gameMap.getSite(otherLocation);
        if (site.owner == myID) {
          nextFrontier.add(otherLocation);
        }
      }
    }
    fillDistanceToBorder(distanceToBorder, nextFrontier, distance + 1);
  }

  private Set<Location> findMyFrontier(Location startingLocation) {
    boolean[][] hasBeenQueued = new boolean[gameMap.width][gameMap.height];

    Queue<Location> nonFrontier = new LinkedList<>();
    Set<Location> frontier = new HashSet<>();
    hasBeenQueued[startingLocation.x][startingLocation.y] = true;
    nonFrontier.add(startingLocation);

    Location location = nonFrontier.poll();
    while (location != null) {
      for (Direction direction : Direction.CARDINALS) {
        Location otherLocation = gameMap.getLocation(location, direction);
        if (hasBeenQueued[otherLocation.x][otherLocation.y]) {
          continue;
        }
        hasBeenQueued[otherLocation.x][otherLocation.y] = true;

        Site site = gameMap.getSite(otherLocation);
        if (site.owner != myID) {
          nonFrontier.add(otherLocation);
        } else {
          frontier.add(otherLocation);
        }
      }

      location = nonFrontier.poll();
    }

    return frontier;
  }

  public void applyTo(GameMap gameMap, Location location, MoveMap moveMap) {
    Site site = gameMap.getSite(location);

    if (site.owner == myID && site.strength / site.production > MIN_STRENGTH_RATIO) {
      moveMap.putAdd(toClosestBorder.get(location), WEIGHT);
    }
  }

  public static void main(String[] args) throws java.io.IOException {
    GameMap gameMap = new GameMap.Builder(10, 10)
      .addSite(2, 9, 1, 0, 0)
      .addSite(3, 9, 1, 0, 0)
      .addSite(4, 9, 1, 0, 0)
      .addSite(2, 0, 1, 0, 0)
      .addSite(3, 0, 1, 0, 0)
      .addSite(4, 0, 1, 0, 0)
      .addSite(2, 1, 1, 0, 0)
      .addSite(3, 1, 1, 0, 0)
      .addSite(4, 1, 1, 0, 0)
      .build();

    MigrateStrengthToBorderHeuristic heuristic = new MigrateStrengthToBorderHeuristic(gameMap, 1);
  }
}
