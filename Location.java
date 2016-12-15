import java.util.Comparator;

public class Location {
  public int x, y;

  public Location(int x_, int y_) {
    x = x_;
    y = y_;
  }
  public Location(Location l) {
    x = l.x;
    y = l.y;
  }

  public int x() {
    return x;
  }
  public int y() {
    return y;
  }

  public int squareDistanceTo(Location that) {
    int deltaX = that.x - this.x;
    int deltaY = that.y - this.y;
    return (deltaX * deltaX) + (deltaY * deltaY);
  }

  // Lazy initialize to save memory
  private Comparator<Location> distanceComparator;

  public Comparator<Location> comparingByDistance() {
    if (distanceComparator != null) {
      return distanceComparator;
    }
    distanceComparator = new Comparator<Location>() {
      public int compare(Location one, Location two) {
        return two.squareDistanceTo(Location.this) - one.squareDistanceTo(Location.this);
      }
    };
    return distanceComparator;
  }
}
