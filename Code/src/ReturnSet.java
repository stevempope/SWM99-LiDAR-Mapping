package lidarMapping;

import java.util.ArrayList;

/**
 * A ReturnSet is a collection of LReturns that correspond to the output from a 
 * single sense from the LiDAR sensor. This is used to distinguish from and allow
 * input from multiple senses to be laid over each other or amalgamated.
 * 
 * ReturnSets are sensitve to orientation, meaning it is possible to have multiple
 * sensors providing their own ReturnSets from different orientations and still have an operable
 * map.
 * 
 * ReturnSets store an ArrayList of LReturns and the orientation of those returns.
 * 
 * @author Stephen Pope 15836791
 * @version 0.2
 */

public class ReturnSet {
	private Orientation theOrientation;
	private ArrayList<LReturn> blockageList;
	private Waypoint position;

	/**
	 * The base constructor for a ReturnSet. This assumes an anti-clockwise rotation.
	 */
	public ReturnSet() {
		theOrientation = Orientation.antiClockwise;
		blockageList = new ArrayList<LReturn>();
	}
	
	/**
	 * The regular constructor for a returnSet. Currently this only would be used when
	 * a clockwise sensor is in use, but again when more sensor modes are added, this
	 * becomes more relevant.
	 * @param direction
	 */
	public ReturnSet (Orientation direction) {
		theOrientation = direction;
		blockageList = new ArrayList<LReturn>();
	}

	/**
	 * Adds an LReturn (Blockage) to the list of blockages
	 * @param block - The new LReturn
	 * @return true
	 */
	public void addBlockage(LReturn block) {
		blockageList.add(block);
	}

	public Orientation getOrientation() {
		return theOrientation;
		
	}
	
	public ArrayList<LReturn> getBlockages(){
		return blockageList;
		
	}
	
	public void removeReturn(LReturn tbr) {
		blockageList.remove(tbr);
	}
	
	public void removeAll() {
		blockageList.clear();
	}
	
	public void setPosition(Waypoint pos) {
		position = pos;
	}
	
	public Waypoint getPosition() {
		return position;
	}
}
