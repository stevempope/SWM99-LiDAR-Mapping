package lidarMapping;

import java.util.ArrayList;

/**
 * The Map is a collection of ReturnSets, representing the set containing all LiDAR returns.
 * This allows input from multiple sensors to exist in the same space.
 * 
 * @author Stephen Pope 15836791
 * @version 0.3
 *
 */
public class Map {
	private ArrayList<ReturnSet> reads;
	private Waypoint translation;
	
	/**
	 * Map constructor - initialises storage ready for a LiDAR return
	 */
	public Map() {
		reads = new ArrayList<ReturnSet>();	
		translation = new Waypoint(0,0);
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
	
	public void transformMap (Waypoint newLocation) {
		CartesianPair prev = new CartesianPair(translation);
		CartesianPair curr = new CartesianPair(newLocation);
		CartesianPair diff = new CartesianPair();
		diff.setX(prev.getX() - curr.getX());
		diff.setY(prev.getY() - curr.getY());
		for (ReturnSet r : getBlockages()) {
			for(LReturn l : r.getBlockages()) {
				l = transformDist(l, diff, newLocation.getAngle());
			}
		}
	}
	
	public LReturn transformDist (LReturn l, CartesianPair transformation, Integer ang) {
		Integer pos = 0;
		Waypoint w = new Waypoint();
		for (Integer d : l.getBlocks()) {
			w.setAngle(l.getStart() + pos);
			w.setDistance(d);
			CartesianPair c = new CartesianPair(w);
			c.setX(c.getX() + transformation.getX());
			c.setY(c.getY() + transformation.getY());
			Waypoint z = new Waypoint(c);
			d = z.getDistance();
			pos++;
		}
		l.setStart((l.getStart() + ang) % 360);
		l.setEnd((l.getEnd() + ang) % 360);
		return l;
	}


	public Waypoint getTranslation() {
		return translation;
	}
	
}
