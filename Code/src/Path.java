package lidarMapping;

import java.util.ArrayList;

public class Path {
	private ArrayList<Waypoint> thePath;

	public Path() {
		thePath = new ArrayList<Waypoint>();
	}

	public boolean addWaypoint(Waypoint wp) {
		thePath.add(wp);
		return true;
	}

	public boolean insertIntoPath(Integer loc, Waypoint wp) {
		thePath.add(loc, wp);
		return true;
	}

	public boolean removePathWaypoint(int loc) {
		thePath.remove(loc);
		return true;
	}

	public ArrayList<Waypoint> getPath() {
		return thePath;

	}

	public Waypoint popNextWaypoint() {
		if (thePath.size() > 0 ) {
			Waypoint x = thePath.get(0);
			thePath.remove(0);
			return x;
		}
		else return null;
	}
	
	public boolean clearPath() {
		thePath.clear();
		return true;
	}
}

