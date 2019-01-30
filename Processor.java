package lidarMapping;

public class Processor {
	
	VLsensor theSensor;
	ReturnSet theResultSet;
	Integer counter;
	Integer agentSize;
		
	public Processor() {
	}
	
	public Processor (VLsensor vls, Integer size) {
	counter = 0;
	agentSize =  size;
	theSensor = vls;
	theResultSet = new ReturnSet(theSensor.getOrientation());
	}
	
	public boolean updateMap(Map theMap) {
		return theMap.addScan(this.scanEnvironment(theResultSet));
		
	}
	
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
			//otherwise throw away
		}
		return theResultSet;
	}

}
