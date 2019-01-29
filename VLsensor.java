package lidarMapping;

import java.util.ArrayList;

public class VLsensor {
	int counter;
	ArrayList<int []> res;
	int [] sense1;
	int [] sense2;
	int [] sense3;
	int [] sense4;
	
	public VLsensor() {
		counter = 0;
		res = new ArrayList <int []>();
		sense1 = new int [] {1,2}; //set1 needs to be 360
		res.add(sense1);
		sense2 = new int [] {2,3,4,5,6,7,8,9};
		res.add(sense2);//etc
		sense3 = new int [] {5};
		res.add(sense3);
		sense4 = new int [] {10,15,10000,23};
		res.add(sense4);
	}
	
	public int [] sense(int index){
		return res.get(index);
	}

}
