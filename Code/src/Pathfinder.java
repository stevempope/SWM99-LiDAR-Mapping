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
		thePath.clearPath();
		best = new Waypoint(0,200000000);
		thePath.addWaypoint(destination);
		if (lineOfSight(destination, theMap) == false) {
			for (ReturnSet r: theMap.getBlockages()) {
				for(LReturn l: r.getBlockages()) {
					l.setStartScore(l.getStartDist() + cosRule(l.getStart(), l.getStartDist(), destination));
					l.setEndScore(l.getEndDist() + cosRule(l.getEnd(), l.getEndDist(), destination));
					if(l.getStartScore() <= best.getDistance()) {
						best.setAngle(l.getStart());
						best.setDistance(l.getStartDist());
						System.out.printf("%d , %d \n", best.getAngle(), best.getDistance());
					}
					if(l.getEndScore() <= best.getDistance()) {
						best.setAngle(l.getEnd());
						best.setDistance(l.getEndDist());
						System.out.printf("%d , %d \n", best.getAngle(), best.getDistance());
					}
				}
			}
			thePath.insertIntoPath(0, best);
		}
		for(Waypoint w : thePath.getPath()) {
			System.out.printf("%d , %d \n", w.getAngle(), w.getDistance());
		}
		return thePath;
	}

	private boolean lineOfSight(Waypoint destination, Map theMap) {
		System.out.println("LOS TEST");
		boolean los = true;
		for (ReturnSet r : theMap.getBlockages()) {
			for(LReturn l : r.getBlockages()) {
				if (l.getStart() < destination.getAngle() && l.getEnd() > destination.getAngle()){
					if(l.getDistance(destination.getAngle()-l.getStart()) < destination.getDistance()) {
						los = false;
					}
				}
			}
		}
		System.out.println(los);
		return los;
	}
	
	private double cosRule(Integer angle, Integer distance, Waypoint dest) {
		int a =  distance;
		int c = dest.getDistance();
		int A = Math.abs(dest.getAngle()-angle);
		double b = ((a*a)+(c*c))-((2*a*c)*(Math.cos(Math.toRadians(A))));
		b = Math.sqrt(b);
		return b;
	}
}
