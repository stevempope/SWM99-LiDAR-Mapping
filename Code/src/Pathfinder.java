package lidarMapping;

public class Pathfinder {
	private Waypoint best;
	private Path thePath;

	public Pathfinder() {
		thePath = new Path();
	}

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
			//For each LReturn in a set, we need to score that set based on its proximity to the destination
		}

		//Pathfinding Algorithm
		//NEED A WAY OF ADDRESSING THE ANGLE

		return thePath;
	}

}
