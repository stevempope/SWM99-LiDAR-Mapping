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
		double bestScore = best.getDistance();
		thePath.addWaypoint(destination);
		if (lineOfSight(destination, theMap) == false) {
			CartesianPair dest = new CartesianPair(destination);
			CartesianPair startOffset = new CartesianPair();
			CartesianPair endOffset = new CartesianPair();
			for (ReturnSet r: theMap.getBlockages()) {
				for(LReturn l: r.getBlockages()) {
					CartesianPair startNode = new CartesianPair(new Waypoint (l.getStart(), l.getStartDist()));
					startOffset.setX(startNode.getX()-dest.getX());
					startOffset.setY(startNode.getY()-dest.getY());
					CartesianPair endNode = new CartesianPair(new Waypoint (l.getEnd(), l.getEndDist()));
					endOffset.setX(endNode.getX()-dest.getX());
					endOffset.setY(endNode.getY()-dest.getY());
					l.setStartScore(Math.sqrt((startOffset.getX() * startOffset.getX())+ (startOffset.getY() * startOffset.getY())));
					System.out.printf("Start Score: Root  %f squared + %f squared  = %f \n", startOffset.getX(), startOffset.getY(), l.getStartScore());
					l.setEndScore(Math.sqrt((endOffset.getX() * endOffset.getX())+ (endOffset.getY() * endOffset.getY())));
					System.out.printf("End Score: Root  %f squared + %f squared  = %f \n", endOffset.getX(), endOffset.getY(), l.getEndScore());
					if(l.getStartScore() <= bestScore) {
						best.setAngle(l.getStart());
						best.setDistance(l.getStartDist());
						bestScore = l.getStartScore();
						System.out.printf("%d , %d \n", best.getAngle(), best.getDistance());
					}
					if(l.getEndScore() <= bestScore) {
						best.setAngle(l.getEnd());
						best.setDistance(l.getEndDist());
						bestScore = l.getEndScore();
						System.out.printf("%d , %d \n", best.getAngle(), best.getDistance());
					}
					startOffset.setX(0.0);
					startOffset.setY(0.0);
					endOffset.setX(0.0);
					endOffset.setY(0.0);
					System.out.println("Pass");
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
}
