import java.util.Random;

public enum Direction {
  STILL, NORTH, EAST, SOUTH, WEST;

  public static final Direction[] DIRECTIONS = new Direction[]{STILL, NORTH, EAST, SOUTH, WEST};
  public static final Direction[] CARDINALS = new Direction[]{NORTH, EAST, SOUTH, WEST};

  private static Direction fromInteger(int value) {
    if(value == 0) {
      return STILL;
    }
    if(value == 1) {
      return NORTH;
    }
    if(value == 2) {
      return EAST;
    }
    if(value == 3) {
      return SOUTH;
    }
    if(value == 4) {
      return WEST;
    }
    return null;
  }

  public static Direction randomDirection() {
    return fromInteger(new Random().nextInt(5));
  }

  public Direction reverse() {
    switch (this) {
      case STILL:
        return STILL;
      case EAST:
        return WEST;
      case WEST:
        return EAST;
      case NORTH:
        return SOUTH;
      case SOUTH:
        return NORTH;
      default:
        throw new RuntimeException("Unknown direction " + this.name());
    }
  }
}
