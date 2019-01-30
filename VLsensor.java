package lidarMapping;

import java.util.ArrayList;

/**
 * @author Stephen Pope - 15836791 - s.pope1@uni.brighton.ac.uk
 * @version 0.2
 * 
 * A virtual LiDAR Sensor class.
 * 
 * A virtual LiDAR sensor is produced in place of a real one. This makes it easy to produce reliable test data
 * for our model.
 * 
 * A LiDAR sensor provides our system with two things we care about. The first is the distance data, which
 * assumes one full rotation per sense. To make the sensor more detailed, add more returns. 360 = one read per
 * degree. Distances are assumed to be in Millimetres (mm). 10 mm = 1 cm.
 * 
 * The second is the rotational order of the sensor. Regardless of rotation, the angle of read still counts
 * upwards from 0, but the location of that data in the view and in the model could be flipped had we not recorded
 * it.
 */

public class VLsensor {
	Orientation myOrientation;
	ArrayList<int []> res;									//Stream of 'Sensed' test data.
	int [] sense1;											//Test data sets for consecutive reads.
	int [] sense2;
	int [] sense3;
	int [] sense4;
	
	/**
	 * Sensor constructor.
	 * As no data is given, assume anti-clockwise orientation and fill the arrayList accordingly.
	 * 
	 * @return a VLsensor object
	 */
	
	public VLsensor() {
		myOrientation = Orientation.antiClockwise;
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
	
	/**
	 * Sensor with orientation constructor.
	 * As with blank constructor, however orientation is set to provided value.
	 * 
	 * @return a VLsensor object
	 */
	
	public VLsensor(Orientation orient) {
		myOrientation = orient;
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
	
	/**
	 * Sense method takes a value and returns the stream associated with that set in the ArrayList. Typical
	 * tests will increment the number and call again to get to the next read.
	 * 
	 * @param the index of the sense set in the ArrayList.
	 * @return the Stream of test LiDAR data.
	 */
	public int [] sense(int index){
		return res.get(index);
	}
	
	/**
	 * Returns the orientation of the sensor so that the processor can lay the Map out correctly.
	 * 
	 * @return the sensor orientation.
	 */
	public Orientation getOrientation() {
		return myOrientation;
	}

}
