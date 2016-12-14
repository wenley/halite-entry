import java.util.ArrayList;

public class MyBot {
    public static void main(String[] args) throws java.io.IOException {
        InitPackage iPackage = Networking.getInit();
        int myID = iPackage.myID;
        GameMap gameMap = iPackage.map;
        GrowthStrategy strategy;

        Networking.sendInit("MyJavaBot");

        while(true) {
            ArrayList<Move> moves = new ArrayList<Move>();

            gameMap = Networking.getFrame();
            strategy = new GrowthStrategy(gameMap);

            for(int y = 0; y < gameMap.height; y++) {
                for(int x = 0; x < gameMap.width; x++) {
                    Site site = gameMap.getSite(new Location(x, y));
                    if(site.owner == myID) {
                        Direction dir = strategy.recommendedDirection(x, y);
                        moves.add(new Move(new Location(x, y), dir));
                    }
                }
            }
            Networking.sendFrame(moves);
        }
    }
}
