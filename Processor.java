package lidarMapping;

public class Processor {
	
	VLsensor theSensor;
	ReturnSet theMap;
	int counter;
	int agentSize;
		
	public Processor() {
		
	}
	
	public Processor (VLsensor vls, int size) {
	counter = 0;
	agentSize =  size;
	theSensor = vls;
	theMap = new ReturnSet(theSensor.getOrientation());
	}
	
	public ReturnSet scanEnvironment(ReturnSet map) {
		int [] workingSet = theSensor.sense(counter);
		counter++;
		for (int i : workingSet) {
			//logic for gaps
		}
		return null;
	}

}
