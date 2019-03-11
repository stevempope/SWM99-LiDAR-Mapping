package lidarMapping;

import java.util.ArrayList;

/**
 * The Map is a collection of ReturnSets, representing the set containing all LiDAR returns.
 * This allows input from multiple sensors to exist in the same space.
 * 
 * @author Stephen Pope 15836791
 * @version 0.4
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
	
	/**
	 * Transforms the existing Map.
	 * 
	 * When an agent moves from their point of origin and takes a new scan, the data we have existing in the map must be transformed so that it reflects
	 * its "real" location in the world (relative to our current location)
	 * 
	 * This method iterates through each LiDAR return and calls the transform method.
	 * 
	 * @param newLocation - the angle and distance our map data is now offset by.
	 */
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
		translation = new Waypoint(curr);
	}
	
	/**
	 * Transforms a single LReturn.
	 * 
	 * We convert to a Waypoint and then a Cartesian Pair (Which naturally generates our current XY).
	 * We then convert back to a Waypoint (The constructors convert between XY and AD) and extract the distance value of the return.
	 * The angle is a simple addition and modulus divide by 360 to get the amount the angle will change by.
	 * @param l - our LReturn
	 * @param transformation -  the XY pair representing the difference between our current and last position.
	 * @param ang -  the angle we are now facing at our current location
	 * @return the transformed LReturn
	 */
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
