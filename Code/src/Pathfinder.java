package lidarMapping;

/**
 * The Pathfinder calculates the optimal route to the destination in the environment
 * represented by the Map
 * 
 * The algorithm used currently is best-first
 * 
 * The Pathfinder produces a path consisting of Waypoints (Turning locations along
 * that path. As we can move to any any we know the turning point should be the
 * optimal place)
 * 
 * @author Stephen Pope 15836791
 * @version 0.1
 */

public class Pathfinder {
	private Waypoint best;
	private Path thePath;

	/**
	 * Constructor for a Pathfinder that initialies a new Path.
	 * Even if the Path ends up with nothing in it we return an
	 * empty path
	 */
	public Pathfinder() {
		thePath = new Path();
	}
	
	/**
	 * Our pathfinding algorithm
	 * Currently, if the environment is empty (no blockages in range), then we move
	 * straight to our destination
	 * 
	 * If there are blockages, we score them all and then chose the one with the shortest
	 * distance to the destination (currently flawed as if we have line of sight on the
	 * destination we should still proceed to it.
	 * 
	 * @param theMap - The environment dataset
	 * @param destination - Where we want to go as a Waypoint
	 * @param currentPosition - Where we are currently in the world
	 * @return - The calculated path
	 */
	public Path pathfind(Map theMap, Waypoint destination, Waypoint currentPosition) {
		Map world = theMap;
		if (world.getBlockages().isEmpty()) {
			thePath.addWaypoint(destination);
		}
		else {
			thePath.clearPath();
			best = new Waypoint(0, 200000);
			for (ReturnSet r: theMap.getBlockages()) {
				for(LReturn l: r.blockageList) {
					l.setStartScore(l.getStartDist());
					l.setEndScore(l.getEndDist());
					if(l.getStartScore() <= best.getDistance()) {
						best.setAngle(l.getStart());
						best.setDistance(l.getStartDist());
					}
					if(l.getEndScore() <= best.getDistance()) {
						best.setAngle(l.getEnd());
						best.setDistance(l.getEndDist());
					}
				}
			}
			thePath.addWaypoint(best);
		}
		return thePath;
	}

}
