public class Move {
  public Location loc;
  public Direction dir;

  public Move(Location loc_, Direction dir_) {
    loc = loc_;
    dir = dir_;
  }

  public String toString() {
    return String.format("%s -> %s", loc, dir.name());
  }
}
