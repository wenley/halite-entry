import java.util.function.Function;

public class MassSquareDistance implements Function<Location, Long> {
  private final GameMap gameMap;
  private final int owner;

  public MassSquareDistance(GameMap gameMap, int owner) {
    this.gameMap = gameMap;
    this.owner = owner;
  }

  @Override
  public Long apply(Location center) {
    long value = 0;

    for (int y = 0; y < gameMap.height; y++) {
      for (int x = 0; x < gameMap.width; x++) {
        Location cell = new Location(x, y);
        Site site = gameMap.getSite(cell);

        if (site.owner == owner) {
          value += site.strength * gameMap.squareDistanceBetween(center, cell);
        }
      }
    }

    return value;
  }
}
