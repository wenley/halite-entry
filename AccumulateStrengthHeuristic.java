// Bulk up weak sites
// Vulnerable to invaders; want to move losers towards invaders
public class AccumulateStrengthHeuristic implements GrowthStrategy.Heuristic {
  public static final AccumulateStrengthHeuristic INSTANCE = new AccumulateStrengthHeuristic();

  private static final double WEIGHT = 1.0;
  private static final int MIN_STRENGTH_RATIO = 5;

  private AccumulateStrengthHeuristic() {
  }

  public void applyTo(GameMap gameMap, Location location, MoveMap moveMap) {
    Site site = gameMap.getSite(location);

    if (site.strength < site.production * MIN_STRENGTH_RATIO) {
      moveMap.putAdd(Direction.STILL, WEIGHT);
    }
  }
}
