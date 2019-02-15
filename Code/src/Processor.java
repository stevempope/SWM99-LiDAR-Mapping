package lidarMapping;

/**
 * The processor is responsible for taking the output of a VLSensor and turning that
 * into a ReturnSet. It does this by generating LReturns and storing them in the ReturnSet
 * 
 * What consitutes a blockage in the environment is solely dependent on the size and ability
 * of the pathfinding agent. In this model we are only concerned with the size of the agent.
 * The size reflects the physical size plus the turning circle of the vehicle
 * 
 * We also have a counter to give us a value to keep track of the location of the previous return
 * This is so that we can set the end of a blockage
 * 
 * @author Stephen Pope 15836791
 *
 */

public class Processor {
	
	VLsensor theSensor;
	ReturnSet theResultSet;
	Integer counter;
	Integer agentSize;
	
	/**
	 * Deprecated?
	 */
	public Processor() {
	}
	
	/**
	 * The constructor for a Processor
	 * @param vls - the Virtual LiDAR sensor we will recieve input from
	 * @param size - The size of the pathfinding agent
	 */
	public Processor (VLsensor vls, Integer size) {
	counter = 0;
	agentSize =  size;
	theSensor = vls;
	theResultSet = new ReturnSet(theSensor.getOrientation());
	}
	
	/**
	 * Adds a ResultSet to the Map
	 * @param theMap - the Map we are appending to
	 * @return result of the addition operation
	 */
	public boolean updateMap(Map theMap) {
		return theMap.addScan(this.scanEnvironment(theResultSet));
		
	}
	
	/**
	 * The core logic of a processor.
	 * There are some major conditions we consider when looking at the environment:
	 * 
	 * The first is when we discover that there is a difference between 2 returns
	 * larger than the size of the agent (a gap). In this case there are 2 scenarios:
	 * 
	 * - We are not currently tracking a blockage, so this must be the start of a new
	 * one. We create a new LReturn and set our current values as the start of the blockage
	 * 
	 * - We were tracking an object and must therefore be at its end. In this case, we want
	 * to set the end to the previous return we were considering. We add the blockage to the
	 * LReturn list and clear the pointer to blockage, so that the next value we encounter
	 * create a new LReturn
	 * 
	 * The second case is where we discover a value within our size. If we are tracking an object
	 * then we add it to our working list. If we weren't tracking an object, this code would be
	 * unreachable, so the only condition that means we get this far is if we encounter a value
	 * 0, indicating we are beyond the range of the sensor. In this case we ignore the return and
	 * load the next one
	 * @param map - the Map
	 * @return the ResultSet of blockages
	 */
	public ReturnSet scanEnvironment(ReturnSet map) {
		Integer [] workingSet = theSensor.sense(counter);
		Integer prev = 0;
		counter++;
		LReturn blockage = new LReturn();
		for (Integer i = 0; i > workingSet.length; i++) {
			if ((Math.abs(prev-workingSet[i]) > agentSize)) { //gap detected
				if(blockage.getStart() == null) {  //if not in snake
					blockage = new LReturn(i,workingSet[i]);
					prev = workingSet[i];
				}
				else if (blockage.getEnd() == null) { //if in unfinished snake
					blockage.setEnd(i-1);
					theResultSet.addBlockage(blockage);
					prev = workingSet[i];
					blockage = null;
				}
			}
			else if (blockage.getStart() != null && blockage.getEnd() == null){ //gap not detected, in snake
				blockage.setDistance(workingSet[i]);
			}
			//otherwise throw away or ignore
		}
		return theResultSet;
	}

}
