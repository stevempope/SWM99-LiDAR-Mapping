package lidarMapping;

import java.util.ArrayList;

public class LReturn {
	private Integer startAngle;
	private Integer startScore;
	private Integer endAngle;
	private Integer endScore;
	private ArrayList<Integer> returnList;
	
	public LReturn () {
		returnList = new ArrayList<Integer>();
	}
	
	public LReturn (Integer start, Integer blockage) {
		startAngle = start;
		returnList = new ArrayList<Integer>();
	}
	
	public LReturn (Integer start, ArrayList<Integer> blocks, Integer end) {
		startAngle = start;
		returnList = blocks;
		endAngle = end;
	}
	
	public Integer getStart() {
		return startAngle;
	}
	
	public boolean setStart(Integer start) {
		startAngle = start;
		return true;
	}
	
	public Integer getEnd() {
		return endAngle;
	}
	
	public boolean setEnd(Integer end) {
		endAngle = end;
		return true;
	}
	
	public Integer getDistance(Integer theAngle) {
		return returnList.get(theAngle);
	}
	
	public boolean setDistance(Integer theDistance) {
		returnList.add(theDistance);
		return true;
	}
	
	public Integer getStartScore() {
		return startScore;
	}
	
	public boolean setStartScore(Integer score) {
		startScore = score;
		return true;
	}
	
	public Integer getEndScore() {
		return endScore;
	}

	public boolean setEndScore(Integer score) {
		endScore = score;
		return true;
	}
	
	public Integer getStartDist() {
		return returnList.get(0);
	}
	
	public Integer getEndDist() {
		return returnList.get(returnList.size() -1);
	}
}
