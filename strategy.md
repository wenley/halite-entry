
# Overall Observations

Operates with these intuitions:
1. Want to maximize time-discounted growth
2. Want to hold a strong frontier to avoid take-over
3. Edge growth is more valuable than center growth, since the center takes time to travel to the edge
3.5. Center growth can be sent where it's most needed most quickly
4. Want to distribute strength towards strongest enemy
4.5. Want to distribute enough strength towards weakest enemy

Property: Your cells will never end directly adjacent to enemy
Always have a distance. Best you can do is point into a divot in the enemy
Want your point against their flank

### Tactics

- Never attack a neutral territory unless you can take it over
- Minimize surface area of attack - want to take over band
- Attack with kamikaze - intentionally surrounded by weak enemy territory to destroy production at 4x value
- Want to always have more production than opponent
- Want to maximize relative value of strength
- Given you can't stop enemy attacks, should always send towards enemy
- Best to have pointy formation towards enemy, lowest overkill by opponent
- Pointy and strong followed by broad sweep to claim production

### Heuristics

1. Maximize relative production - if you are producing more than opponents at all times, you win
2. Maximize utilization of strength - must make use of strength to achieve #1
  - Capture neutral territory
  - Destroy opponents strength
3. Balance holdings between production and strength
  - Over-investing in short-term production minimizes long-term production by opportunity costs
  - Need to account for time-discounted gains of today's STILL vs. MOVE

Best defense is good offense - maximize destructive power of your strength
- Prevents hostile takeovers

Acquiring production requires consuming strength
Defending requires consuming strength
Producing strength requires not expanding

# Strategy 1

Each cell balances forces and global calculations to determine its move
Calculations:
- Relative overall "power" of each opponent (target direction)
- Where cheapest (time-discounted) growth territory
- Danger zones (large enemy influence)
- Opportunity zones (minimal enemy influence)

# Strategy 2

Totally different approach: Sources + Sinks, forces in directions

- Enemies are sinks
- Different attractions depending no size of a blob
- Small blobs are not attracted to anything to grow up

Example heuristics:
- Big blobs repel each other at close distances (short, powerful length scale)
- Enemy blobs attract your big blobs (medium length scale)
- High production areas attract blobs (diminishing effects for larger blobs)
- Attraction dies off quickly (inverse square law? to avoid moving in inefficient ways)


