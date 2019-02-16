package lidarMapping;

import java.util.ArrayList;

/**
 * A Path is a collection of Waypoints, followed by an agent to a destination
 * @author Stephen Pope 15836791
 * @version 0.1
 */

public class Path {
	private ArrayList<Waypoint> thePath;
	
	/**
	 * Constructor for a Path, initialises storage. There may be a situation
	 * where we hand back an empty path (if there is no valid route to our
	 * destination
	 */
	public Path() {
		thePath = new ArrayList<Waypoint>();
	}

	/**
	 * Adds a Waypoint to the Path
	 * @param wp - the new Waypoint
	 * @return true
	 */
	public boolean addWaypoint(Waypoint wp) {
		thePath.add(wp);
		return true;
	}

	/**
	 * Adds a Waypoint at a specific location. This is used if we encounter
	 * a blockage at our location
	 * @param loc - the location of the blockage along the list in the Path
	 * @param wp - the physical location of the blockage
	 * @return true
	 */
	public boolean insertIntoPath(Integer loc, Waypoint wp) {
		thePath.add(loc, wp);
		return true;
	}

	/**
	 * Removes a Waypoint from the path. This may be used to counter a moving
	 * object.
	 * @param loc -location of Waypoint in the list to be removed
	 * @return true
	 */
	public boolean removePathWaypoint(int loc) {
		thePath.remove(loc);
		return true;
	}
	
	/**
	 * Gets the current Path
	 * @return the Path
	 */
	public ArrayList<Waypoint> getPath() {
		return thePath;

	}
	
	/**
	 * Pops the first item in the Path off the list
	 * @return the Popped Waypoint
	 */
	public Waypoint popNextWaypoint() {
		if (thePath.size() > 0 ) {
			Waypoint x = thePath.get(0);
			thePath.remove(0);
			return x;
		}
		else return null;
	}
	
	/**
	 * Empties the Path of all Waypoints. Used if we need to recalculate the Path
	 * @return true
	 */
	public boolean clearPath() {
		thePath.clear();
		return true;
	}
}

