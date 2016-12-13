import java.util.ArrayList;

/**
 * Operates with these intuitions:
 * 1. Want to maximize time-discounted growth
 * 2. Want to hold a strong frontier to avoid take-over
 * 3. Edge growth is more valuable than center growth, since the center takes time to travel to the edge
 * 3.5. Center growth can be sent where it's most needed most quickly
 * 4. Want to distribute strength towards strongest enemy
 * 4.5. Want to distribute enough strength towards weakest enemy
 *
 * Property: Your cells will never end directly adjacent to enemy
 * Always have a distance. Best you can do is point into a divot in the enemy
 * Want your point against their flank
 *
 * Tactics:
 * Never attack a neutral territory unless you can take it over
 * Minimize surface area of attack - want to take over band
 * Attack with kamikaze - intentionally surrounded by weak enemy territory to destroy production at 4x value
 * Want to always have more production than opponent
 * Want to maximize relative value of strength
 * Given you can't stop enemy attacks, should always send towards enemy
 * Best to have pointy formation towards enemy, lowest overkill by opponent
 * Pointy and strong followed by broad sweep to claim production
 *
 * Heuristics:
 * Maximize production
 * Maximize strength
 * Balance holdings between production and strength
 *
 * Each cell balances forces and global calculations to determine its move
 *
 * Acquiring production requires consuming strength
 * Defending requires consuming strength
 * Producing strength requires not expanding
 *
 * Totally different approach: Sources + Sinks, forces in directions
 * Enemies are sinks
 * Different attractions depending no size of a blob
 * Small blobs are not attracted to anything
 * Big blobs repel each other
 * Enemy blobs attract your big blobs
 * High production areas attract blobs
 * Attraction dies off quickly (to avoid moving in inefficient ways)
 */
public class MyBot {
    public static void main(String[] args) throws java.io.IOException {
        InitPackage iPackage = Networking.getInit();
        int myID = iPackage.myID;
        GameMap gameMap = iPackage.map;

        Networking.sendInit("MyJavaBot");

        while(true) {
            ArrayList<Move> moves = new ArrayList<Move>();

            gameMap = Networking.getFrame();

            for(int y = 0; y < gameMap.height; y++) {
                for(int x = 0; x < gameMap.width; x++) {
                    Site site = gameMap.getSite(new Location(x, y));
                    if(site.owner == myID) {
                        Direction dir = Direction.randomDirection();
                        moves.add(new Move(new Location(x, y), dir));
                    }
                }
            }
            Networking.sendFrame(moves);
        }
    }
}
