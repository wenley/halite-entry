
public class MinimumMassDistanceWalker {
  private final GameMap gameMap;
  private final int owner;
  private final long[][] cachedEvaluations;

  public MinimumMassDistanceWalker(GameMap gameMap, int owner) {
    this.gameMap = gameMap;
    this.owner = owner;

    cachedEvaluations = new long[gameMap.width][gameMap.height];
  }

  public Location minimumMassDistance(Location startingLocation) {
    Location currentLocation = startingLocation;
    long currentValue = massSquareDistance(currentLocation);
    boolean foundBetterValue = false;

    do {
      foundBetterValue = false;

      for (Direction direction : Direction.CARDINALS) {
        Location adjacentLocation = gameMap.getLocation(currentLocation, direction);
        long adjacentValue = massSquareDistance(adjacentLocation);

        if (adjacentValue < currentValue) {
          currentLocation = adjacentLocation;
          currentValue = adjacentValue;
          foundBetterValue = true;
        }
      }
    } while (foundBetterValue);

    return currentLocation;
  }

  private long massSquareDistance(Location location) {
    long cachedValue = cachedEvaluations[location.x][location.y];
    if (cachedValue != 0) {
      return cachedValue;
    }

    // TODO: Actually compute value

    return 0;
  }
}
