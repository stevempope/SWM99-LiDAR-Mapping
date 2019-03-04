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
 * @version 0.3
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
	 * @return - The calculated path
	 */
	public Path pathfind(Map theMap, Waypoint destination) {
		best = new Waypoint(0,200000000);
		if (theMap.getBlockages().isEmpty() || lineOfSight(destination, theMap) == true) {
			thePath.addWaypoint(destination);
		}
		else {
			thePath.clearPath();
			for (ReturnSet r: theMap.getBlockages()) {
				for(LReturn l: r.getBlockages()) {
					l.setStartScore(l.getStartDist() + cosRule(l.getStart(), l.getStartDist(), destination));
					l.setEndScore(l.getEndDist() + cosRule(l.getEnd(), l.getEndDist(), destination));
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
			thePath.addWaypoint(destination);
			//TODO what if there is no valid path?
		}
		System.out.printf("angle = %s, distance = %d \n",thePath.getPath().get(0).getAngle(), thePath.getPath().get(0).getDistance());
		
		return thePath;
	}

	private boolean lineOfSight(Waypoint destination, Map theMap) {
		System.out.println("LOS TEST");
		for (ReturnSet s : theMap.getBlockages()) {
			for(LReturn m : s.getBlockages()) {
				if (m.getStart() < destination.getAngle() && m.getEnd() > destination.getAngle()){
					if(m.getDistance(destination.getAngle() - m.getStart()) >= destination.getDistance()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private double cosRule(Integer start, Integer startDist, Waypoint destination) {
		Integer a = startDist;
		Integer c = destination.getDistance();
		double b = Math.toRadians(Math.abs(start - destination.getAngle()));
		return Math.sqrt((a*a + c*c) - 2*a*c*(Math.cos(b)));
	}

}
