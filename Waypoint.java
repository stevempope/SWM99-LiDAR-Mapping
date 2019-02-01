package lidarMapping;

public class Waypoint {
	private Integer theAngle;
	private Integer theDistance;
	
	public Waypoint() {
		
	}
	
	public Waypoint(Integer a, Integer d) {
		theAngle = a;
		theDistance = d;
	}
	
	public Integer getAngle() {
		return theAngle;
	}
	
	public Integer getDistance() {
		return theDistance;
	}

}
