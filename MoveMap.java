import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MoveMap {
  private final Map<Direction, Double> map = new HashMap<>();

  public void putAdd(Direction direction, double value) {
    if (!map.containsKey(direction)) {
      map.put(direction, 0.0);
    }
    map.put(direction, map.get(direction) + value);
  }

  public Set<Map.Entry<Direction, Double>> entrySet() {
    return map.entrySet();
  }
}
