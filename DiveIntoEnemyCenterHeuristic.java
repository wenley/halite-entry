import java.util.Arrays;
import java.util.Map;

public class DiveIntoEnemyCenterHeuristic implements GrowthStrategy.Heuristic {
  private static final int STRENGTH_THRESHOLD = 5;
  private static final double WEIGHT = 1.0;

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
      // By default, move away from own cluster
      Direction awayFromSelf = gameMap.moveTowards(playerAveragePositions.positionForOwner(myID).getLocation(), myLocation);
      Direction towardsEnemy = playerAveragePositions.positions().stream()
        .filter(entry -> entry.getKey() != myID)
        .map(Map.Entry::getValue)
        .map(PlayerAveragePositions.Position::getLocation)
        .min(myLocation.comparingByDistance())
        .map(location -> gameMap.moveTowards(myLocation, location))
        .orElse(awayFromSelf);

      moveMap.putAdd(towardsEnemy, numAdjacentEnemies * WEIGHT);
    }
  }
}
