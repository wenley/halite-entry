// Bulk up weak sites
// Vulnerable to invaders; want to move losers towards invaders
public class AccumulateStrengthHeuristic implements GrowthStrategy.Heuristic {
  public static final AccumulateStrengthHeuristic INSTANCE = new AccumulateStrengthHeuristic();

  private AccumulateStrengthHeuristic() {
  }

  public void applyTo(GameMap gameMap, Location location, MoveMap moveMap) {
    Site site = gameMap.getSite(location);

    if (site.strength < site.production * 5) {
      moveMap.putAdd(Direction.STILL, 1.0);
    }
  }
}
