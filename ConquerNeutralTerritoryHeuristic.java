public class ConquerNeutralTerritoryHeuristic implements GrowthStrategy.Heuristic {
  public static final ConquerNeutralTerritoryHeuristic INSTANCE = new ConquerNeutralTerritoryHeuristic();

  private ConquerNeutralTerritoryHeuristic() {
  }

  public void applyTo(GameMap gameMap, Location location, MoveMap moveMap) {
    Site site = gameMap.getSite(location);

    for (Direction direction : Direction.CARDINALS) {
      Site otherSite = gameMap.getSite(gameMap.getLocation(location, direction));
      if (otherSite.owner == 0 && otherSite.strength < site.strength) {
        moveMap.putAdd(direction, 1.0);
      }
    }
  }
}
