import java.util.function.Function;

public class MinimumWalker {
  private final GameMap gameMap;
  private final long[][] cachedEvaluations;
  private final Function<Location, Long> valueAt;

  public MinimumWalker(GameMap gameMap, Function<Location, Long> valueAt) {
    this.gameMap = gameMap;
    this.valueAt = valueAt;

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

    long value = valueAt.apply(location);

    cachedEvaluations[location.x][location.y] = value;

    return value;
  }
}
