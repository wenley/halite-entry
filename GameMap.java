import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class GameMap {
  public ArrayList< ArrayList<Site> > contents;
  public int width, height;

  public GameMap() {
    width = 0;
    height = 0;
    contents = new ArrayList< ArrayList<Site> >(0);
  }

  public GameMap(int width_, int height_) {
    width = width_;
    height = height_;
    contents = new ArrayList< ArrayList<Site> >(0);
    for(int y = 0; y < height; y++) {
      ArrayList<Site> row = new ArrayList<Site>();
      for(int x = 0; x < width; x++) {
        row.add(new Site());
      }
      contents.add(row);
    }
  }

  public static class Builder {
    private int width, height;
    private Map<Location, Site> cells = new HashMap<>();

    public Builder() {
      this(0, 0);
    }

    public Builder(int width, int height) {
      this.width = width;
      this.height = height;
    }

    public GameMap build() {
      GameMap map = new GameMap(width, height);
      for (Map.Entry<Location, Site> entry : cells.entrySet()) {
        Location location = entry.getKey();
        map.contents.get(location.y).set(location.x, entry.getValue());
      }
      return map;
    }

    public Builder withDimensions(int width, int height) {
      this.width = width;
      this.height = height;
      return this;
    }

    public Builder addSite(int x, int y, int owner, int strength, int production) {
      cells.put(new Location(x, y), new Site(owner, strength, production));
      return this;
    }
  }

  private static class LocationIterator implements Iterator<Location> {
    private final GameMap gameMap;
    private int x = 0;
    private int y = 0;

    private LocationIterator(GameMap gameMap) {
      this.gameMap = gameMap;
    }

    @Override public boolean hasNext() {
      return y < gameMap.height;
    }

    @Override public Location next() {
      Location value = new Location(x, y);

      x++;
      if (x == gameMap.width) {
        x = 0;
        y++;
      }

      return value;
    }
  }

  public Iterable<Location> locations() {
    return new Iterable<Location>() {
      public Iterator<Location> iterator() {
        return new LocationIterator(GameMap.this);
      }
    };
  }

  public void log() {
    for (int y = 0; y < height; y++) {
      StringBuilder line = new StringBuilder();
      for (int x = 0; x < width; x++) {
        Site site = getSite(new Location(x, y));
        line.append(String.format("%d ", site.owner));
      }
      Logger.log(line.toString());
    }
  }

  public boolean inBounds(Location loc) {
    return loc.x < width && loc.x >= 0 && loc.y < height && loc.y >= 0;
  }

  public double getDistance(Location loc1, Location loc2) {
    int dx = Math.abs(loc1.x - loc2.x);
    int dy = Math.abs(loc1.y - loc2.y);

    if(dx > width / 2.0) dx = width - dx;
    if(dy > height / 2.0) dy = height - dy;

    return dx + dy;
  }

  public double getAngle(Location loc1, Location loc2) {
    int dx = loc1.x - loc2.x;

    // Flip order because 0,0 is top left
    // and want atan2 to look as it would on the unit circle
    int dy = loc2.y - loc1.y;

    if(dx > width - dx) dx -= width;
    if(-dx > width + dx) dx += width;

    if(dy > height - dy) dy -= height;
    if(-dy > height + dy) dy += height;

    return Math.atan2(dy, dx);
  }

  public Location getLocation(Location loc, Direction dir) {
    Location l = new Location(loc);
    if(dir != Direction.STILL) {
      if(dir == Direction.NORTH) {
        if(l.y == 0) l.y = height - 1;
        else l.y--;
      }
      else if(dir == Direction.EAST) {
        if(l.x == width - 1) l.x = 0;
        else l.x++;
      }
      else if(dir == Direction.SOUTH) {
        if(l.y == height - 1) l.y = 0;
        else l.y++;
      }
      else if(dir == Direction.WEST) {
        if(l.x == 0) l.x = width - 1;
        else l.x--;
      }
    }
    return l;
  }

  public Set<Site> sitesAdjacentTo(Location location) {
    Set<Site> sites = new HashSet<>();
    for (Direction direction : Direction.CARDINALS) {
      sites.add(getSite(getLocation(location, direction)));
    }
    return sites;
  }

  public Site getSite(Location loc, Direction dir) {
    Location l = getLocation(loc, dir);
    return contents.get(l.y).get(l.x);
  }

  public Site getSite(Location loc) {
    return contents.get(loc.y).get(loc.x);
  }

  // Distance between two coordinates, for a given ringSize
  private static int delta(int one, int two, int ringSize) {
    int delta = (one - two + ringSize) % ringSize;
    if (delta * 2 > ringSize) {
      delta = ringSize - delta;
    }
    return delta;
  }

  public int squareDistanceBetween(Location one, Location two) {
    int deltaX = delta(two.x, one.x, width);
    int deltaY = delta(two.y, one.y, height);

    return (deltaX * deltaX) + (deltaY * deltaY);
  }

  public Direction moveTowards(Location source, Location sink) {
    if (source.equals(sink)) {
      return Direction.STILL;
    }

    Map<Direction, Integer> horizontalDistanceByDirection = new HashMap<>();
    horizontalDistanceByDirection.put(Direction.EAST, (sink.x - source.x + width) % width);
    horizontalDistanceByDirection.put(Direction.WEST, (source.x - sink.x + width) % width);

    Map<Direction, Integer> verticalDistanceByDirection = new HashMap<>();
    verticalDistanceByDirection.put(Direction.NORTH, (source.y - sink.y + height) % height);
    verticalDistanceByDirection.put(Direction.SOUTH, (sink.y - source.y + height) % height);

    return Stream.of(
        horizontalDistanceByDirection.entrySet().stream()
          .min(Map.Entry.comparingByValue())
          .get(),
        verticalDistanceByDirection.entrySet().stream()
          .min(Map.Entry.comparingByValue())
          .get()
        )
      .filter(entry -> entry.getValue() != 0)
      .max(Map.Entry.comparingByValue())
      .get().getKey();
  }

  public static void main(String[] args) {
    GameMap map = new GameMap(10, 10);

    Location source = new Location(3, 5);
    Location sink = new Location(5, 6);
    Direction d = map.moveTowards(source, sink);

    System.out.println(String.format("%s to %s is %s", source.toString(), sink.toString(), d.name()));
  }
}
