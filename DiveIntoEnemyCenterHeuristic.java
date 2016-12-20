import java.util.Arrays;
import java.util.Map;

public class DiveIntoEnemyCenterHeuristic implements GrowthStrategy.Heuristic {
  private static final int STRENGTH_THRESHOLD = 5;
  // Weighted slightly more than AccumulateStrength
  private static final double WEIGHT = 1.1;

  private final int myID;
  private final PlayerAveragePositions playerAveragePositions;

  public DiveIntoEnemyCenterHeuristic(int myID, PlayerAveragePositions playerAveragePositions) {
    this.myID = myID;
    this.playerAveragePositions = playerAveragePositions;
  }

  public void applyTo(GameMap gameMap, Location myLocation, MoveMap moveMap) {
    // Don't go picking a fight unless you have the strength
    if (gameMap.getSite(myLocation).strength < STRENGTH_THRESHOLD) {
      return;
    }

    // When surrounded by enemies, move towards their center
    // Enemies are never immediately adjacent
    long numAdjacentEnemies = Arrays.stream(Direction.CARDINALS)
      .map(direction -> {
          Location oneAway = gameMap.getLocation(myLocation, direction);
          Location twoAway = gameMap.getLocation(oneAway, direction);
          return gameMap.getSite(twoAway);
        })
      .map(otherSite -> otherSite.owner)
      .filter(owner -> owner != 0 && owner != myID)
      .count();

    if (numAdjacentEnemies > 0) {
      Logger.log(String.format("Counted %d enemies adjacent to %s", numAdjacentEnemies, myLocation.toString()));
      // By default, move away from own cluster
      Location myCluster = playerAveragePositions.positionForOwner(myID).getLocation();
      Direction awayFromSelf = gameMap.moveTowards(myCluster, myLocation);

      Direction towardsEnemy = playerAveragePositions.positions().stream()
        .filter(entry -> entry.getKey() != myID)
        .map(Map.Entry::getValue)
        .map(PlayerAveragePositions.Position::getLocation)
        .min(myLocation.comparingByDistance())
        .map(location -> {
          Direction d = gameMap.moveTowards(myLocation, location);
          Logger.log(String.format("%s should move %s to %s", myLocation.toString(), d.name(), location));
          return d;
        })
        .orElse(awayFromSelf);

      moveMap.putAdd(towardsEnemy, numAdjacentEnemies * WEIGHT);
    }
  }
}
