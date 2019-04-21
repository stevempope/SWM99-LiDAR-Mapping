package lidarMapping;

public class CartesianPair {
	private double x;
	private double y;
	
	public CartesianPair() {
		x = 0;
		y = 0;
	}
	
	public CartesianPair(Waypoint ad) {	
		x = ad.getDistance() * (Math.cos(Math.toRadians(ad.getAngle())));
		y = ad.getDistance() * (Math.sin(Math.toRadians(ad.getAngle())));
	}

	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setX(double x2) {
		x = x2;
	}
	
	public void setY(double y2) {
		y =y2;
	}
}
