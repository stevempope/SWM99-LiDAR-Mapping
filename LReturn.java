package lidarMapping;

import java.util.ArrayList;

public class LReturn {
	int startAngle;
	int endAngle;
	ArrayList<Integer> returnList;
	
	public LReturn () {
		startAngle = 0;
		endAngle = 0;
		returnList = new ArrayList<Integer>();
	}
	
	public LReturn (int start, ArrayList<Integer> blocks, int end) {
		startAngle = start;
		returnList = blocks;
		endAngle = end;
	}
	
	public int getStart() {
		return startAngle;
	}
	
	public boolean setStart(int start) {
		startAngle = start;
		return true;
	}
	
	public int getEnd() {
		return endAngle;
	}
	
	public boolean setEnd(int end) {
		endAngle = end;
		return true;
	}
	
	public int getDistance(int theAngle) {
		return returnList.get(theAngle);
	}
	
	public boolean setDistance(int theAngle, int theDistance) {
		returnList.add(theAngle, theDistance);
		return true;
	}

}
