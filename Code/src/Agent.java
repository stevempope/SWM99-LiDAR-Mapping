package lidarMapping;

/**
 * A represenation of our Pathfinding Agent
 * 
 * The agent is the physical body that moves around the environment.
 * It's position is tracked using an angle and a distance from its point of origin (starting position).
 * 
 * @author Stephen Pope 15836791
 * @version 0.1
 */

public class Agent {
	private Waypoint position;
	private Waypoint lastPos;
	private Integer size;
	private boolean visibility;
	
	/**
	 * Agent Constructor. Assumes a default size and origin point.	
	 */
	public Agent() {
		position = new Waypoint (0,0);
		lastPos = position;
		size = 10;
		visibility = false;
	}
	
	/**
	 * Sets the new position of the agent in the world. In our theoretical model, this will be the next location on the calculated Path.
	 * @param wp - the new position of the agent.
	 */
	public void setPosition (Waypoint wp) {
		lastPos = position;
		position = wp;
	}
	
	/**
	 * Retrieves the position of the Agent in the world currently.
	 * @return - the Waypoint of our agents current location.
	 */
	public Waypoint getPosition() {
		return position;
	}
	
	/**
	 * Returns the size of the agent in MM.
	 * @return agent size.
	 */
	public Integer getSize() {
		return size;
	}
	
	/**
	 * Changes the size of the agent to a new value in MM. This is important for classification of objects in our environment.
	 * @param the new size in Millimetres.
	 */
	public void setSize(Integer newSize) {
		size = newSize;
	}
	
	public boolean isVisible() {
		return visibility;
	}
	
	public void setVisibility(boolean v) {
		visibility = v;
	}
	
	public Waypoint getLastPos() {
		return lastPos;
	}
}
