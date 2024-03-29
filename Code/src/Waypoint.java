package lidarMapping;

/**
 * @author Stephen Pope 15836791
 * @version 0.1
 * 
 * A Waypoint is a tuple of an Angle and Distance when viewed from a point of origin o.
 * A given Waypoint may be the destination of a pathfinding algorithm or a point along 
 * the path to a destination.
 *
 */
public class Waypoint {
	private Integer theAngle;
	private Integer theDistance;
	
	public Waypoint() {
	}
	
	/**
	 * Constructor for a Waypoint.
	 * @param a the angle from the point of origin
	 * @param d the distance from the point of origin
	 */
	public Waypoint(Integer a, Integer d) {
		theAngle = a;
		theDistance = d;
	}
	
	public Waypoint (CartesianPair p) {
		theAngle = (int) Math.toDegrees(Math.atan2(p.getY(), p.getX()));
		theDistance = (int)(Math.sqrt((p.getY() * p.getY()) + (p.getX() * p.getX())));
	}
	
	/**
	 * Gets the angle of the Waypoint
	 * @return The integer angle
	 */
	public Integer getAngle() {
		return theAngle;
	}
	
    /**
     * Gets the distance of the Waypoint from the origin
     * @return The distance in Millimetres (mm)
     */
	public Integer getDistance() {
		return theDistance;
	}
	
	public void setAngle(Integer a) {
		theAngle = a;
	}
	
	public void setDistance(Integer d) {
		theDistance = d;
	}
}
