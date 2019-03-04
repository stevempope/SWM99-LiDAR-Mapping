package lidarMapping;

import java.util.ArrayList;

/**
 * An LReturn represents the presence of a single blockage detected in a LiDAR Return.
 * Multiple LReturns combine to create a ReturnSet.
 * An LReturn contains the starting angle the block was detected at, a list of distances
 * in order pertaining to that block (starting with the starting angle and sequentially
 * recording space) and finally the ending angle that block occurs at.
 * 
 * For Pathfinding purposes, each blockage also holds two scores, corresponding to its calculated
 * values from the latest run of the pathfinding algorithm.
 * 
 * @author Stephen Pope 15836791
 * @version 0.2
 *
 */

public class LReturn {
	private Integer startAngle;
	private double startScore;
	private Integer endAngle;
	private double endScore;
	private ArrayList<Integer> returnList;

	/**
	 *The blank constructor for an LReturn. Can be used in conjunction with the setter methods
	 *to set incoming values from the VLSensor and Processor	
	 */
	public LReturn () {
		returnList = new ArrayList<Integer>();
	}

	/**
	 * As above but with arguments to get the object started. Normally this is the
	 * constructor we would use
	 * @param start - The starting angle of the blockage
	 * @param blockage - The distance the start of the blockage is away
	 */
	public LReturn (Integer start, Integer blockage) {
		startAngle = start;
		returnList = new ArrayList<Integer>();
	}

	/**
	 * As above but a full arrayList is provided. Used for copying an LReturn
	 * Potentially useful for map rotation later
	 * @param start - The starting angle of the blockage
	 * @param blocks - The ArrayList of Blockage distances
	 * @param end - The ending angle of the blockage
	 */
	public LReturn (Integer start, ArrayList<Integer> blocks, Integer end) {
		startAngle = start;
		returnList = blocks;
		endAngle = end;
	}

	/**
	 * Gets the starting angle
	 * @return the starting angle
	 */
	public Integer getStart() {
		return startAngle;
	}

	/**
	 * Sets the starting angles value to the provided value
	 * @param start - the starting angle of a blockage
	 * @return true
	 */
	public void setStart(Integer start) {
		startAngle = start;
	}

	/**
	 * Gets the ending angle
	 * @return the ending angle
	 */
	public Integer getEnd() {
		return endAngle;
	}

	/**
	 * Sets the ending angles value to the provided value
	 * @param end
	 * @return true
	 */
	public void setEnd(Integer end) {
		endAngle = end;
	}

	/**
	 * Gets a distance from within the blockage at a specified angle
	 * @param theAngle - the selected angle
	 * @return the distance of the blockage at the given angle
	 */
	public Integer getDistance(Integer theAngle) {
		return returnList.get(theAngle);
	}

	/**
	 * Adds a new distance on the end of the blockage list
	 * @param theDistance - new distance in mm
	 * @return true
	 */
	public void setDistance(Integer theDistance) {
		returnList.add(theDistance);
	}

	/**
	 * Returns the calculated pathfinding score for the starting angle
	 * @return the score
	 */
	public double getStartScore() {
		return startScore;
	}
	
	/**
	 * Used by the pathfinding algorithm to set the score for the starting angle
	 * of the blockage
	 * @param score - the calculated score
	 * @return true
	 */
	public void setStartScore(double score) {
		startScore = score;
	}
	
	/**
	 * Gets the calculated pathfinding score for the end of the blockage
	 * @return the score
	 */
	public double getEndScore() {
		return endScore;
	}
	
	/**
	 * Used by the pathfinding algorithm to set the score for the ending angle
	 * of the blockage
	 * @param score - the calculated score
	 * @return true
	 */
	public void setEndScore(double score) {
		endScore = score;
	}
	
	/**
	 * Gets the distance associated with the starting angle (the first in the ArrayList
	 * this cleans up the code to get the first item as it will be more commonly used)
	 * @return the distance to the blockage at the start angle
	 */
	public Integer getStartDist() {
		return returnList.get(0);
	}

	/**
	 * Gets the distance associated with the ending angle (the last in the ArrayList
	 * this cleans up the code to get the last item as it will be more commonly used)
	 * @return the distance to the blockage at the end angle
	 */
	public Integer getEndDist() {
		return returnList.get(returnList.size() -1);
	}
	
	public void appendBlocks(ArrayList<Integer> blocks){
		returnList.addAll(blocks);
	}
	
	public ArrayList<Integer> getBlocks(){
		return returnList;
	}

	public void insertZero() {
		returnList.add(0, 0);
	}
}
