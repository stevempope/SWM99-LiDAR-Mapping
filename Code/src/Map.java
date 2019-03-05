package lidarMapping;

import java.util.ArrayList;

/**
 * The Map is a collection of ReturnSets, representing the set containing all LiDAR returns.
 * This allows input from multiple sensors to exist in the same space.
 * 
 * @author Stephen Pope 15836791
 * @version 0.2
 *
 */
public class Map {
	private ArrayList<ReturnSet> reads;
	
	/**
	 * Map constructor - initialises storage ready for a LiDAR return
	 */
	public Map() {
		reads = new ArrayList<ReturnSet>();		
	}
	
	/**
	 * Add scan adds a return set to the map
	 * @param read - the new ReturnSet to be added
	 * @return true
	 */
	public void addScan(ReturnSet read) {
		reads.add(read);
	}
	
	/**
	 * Empties the Map of all LiDAR returns.
	 * For testing only.
	 * @return true
	 */
	public void clearMap() {
		reads.clear();
	}
	
	/**
	 * Gets the entire set of ReturnSets from the Map for Pathfinding
	 * @return all ReturnSets in the Map
	 */
	public ArrayList<ReturnSet> getBlockages(){
		return reads;
	}
	
	public void translate (Waypoint newLocation) {
		for(ReturnSet r : reads) {
			for (LReturn l : r.getBlockages()) {
				//TODO
			}
		}
	}
}
