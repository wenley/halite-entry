public class Site {
  public int owner, strength, production;

  public Site() {
    this(0, 0, 0);
  }

  public Site(int owner, int strength, int production) {
    this.owner = owner;
    this.strength = strength;
    this.production = production;
  }
}
