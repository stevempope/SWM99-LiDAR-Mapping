package lidarMapping;

public class Pathfinder {
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
			for (ReturnSet r: theMap.getBlockages()) {
				//For each LReturn in a set, we need to score that set based on its proximity to the destination
				//TODO add scoring to an LReturn
			}
		}
		//Pathfinding Algorithm
		return thePath;
	}

}
