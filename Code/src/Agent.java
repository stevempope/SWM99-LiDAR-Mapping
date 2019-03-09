package lidarMapping;

public class Agent {
	private Waypoint position;
	private Integer size;
	
	public Agent() {
		position = new Waypoint (0,0);
		size = 10;
	}

	public void moveTo (Waypoint wp) {
		position = wp;
	}
	
	public Waypoint getPosition() {
		return position;
	}
	
	public Integer getSize() {
		return size;
	}
	
	public void setSize(Integer newSize) {
		size = newSize;
	}
}
