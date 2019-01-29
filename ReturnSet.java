package lidarMapping;

import java.util.ArrayList;

public class ReturnSet {
	Orientation theOrientation;
	ArrayList<LReturn> blockageList;
	
	public ReturnSet() {
		theOrientation = Orientation.antiClockwise;
		blockageList = new ArrayList<LReturn>();
	}
	
	public ReturnSet (Orientation direction) {
		theOrientation = direction;
		blockageList = new ArrayList<LReturn>();
	}

}
