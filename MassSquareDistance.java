import java.util.function.Function;

public class MassSquareDistance implements Function<Location, Long> {
  private final GameMap gameMap;
  private final int owner;

  public MassSquareDistance(GameMap gameMap, int owner) {
    this.gameMap = gameMap;
    this.owner = owner;
  }

  @Override
  public Long apply(Location location) {
    return (long) 0;
  }
}
