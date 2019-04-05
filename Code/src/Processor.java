package lidarMapping;

import java.util.ArrayList;

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
 * @version 0.3
 */

public class Processor {

	private VLsensor theSensor;
	private ReturnSet theResultSet;
	private Agent theAgent;


	/**
	 * The constructor for a Processor
	 * @param vls - the Virtual LiDAR sensor we will recieve input from
	 * @param size - The size of the pathfinding agent
	 */
	public Processor (VLsensor vls, Agent a) {
		theAgent =  a;
		theSensor = vls;
	}

	/**
	 * Adds a ResultSet to the Map
	 * @param theMap - the Map we are appending to
	 * @return result of the addition operation
	 */
	public void updateMap(Map theMap) {
		theResultSet = new ReturnSet(theSensor.getOrientation());
		theMap.addScan(scanEnvironment(theResultSet));
	}

	public void smarterUpdateMap(Map theMap) {
		if(theMap.getBlockages().size() > 0) {
			theMap.clearMap();
		}
		theResultSet = new ReturnSet(theSensor.getOrientation());
		theResultSet = scanEnvironment(theResultSet);
		theResultSet = merge(theResultSet);
		//		theResultSet = blockageAmalgamation(theResultSet);
		theMap.addScan(theResultSet);
	}

	//LOOPS AND MERGES ALL SO ONLY ONE SHOWS ON SCREEN
	//NEED TO ADD COSRULE SO IT CHOOSES RATHER THAN INDISCRIMINATE MERGING
	//ALSO NEEDS TO ADD THE VALUES TO PORT OVER
	private ReturnSet merge(ReturnSet orig) {
		boolean changed = false;
		ReturnSet r = new ReturnSet();
		double cosRule = 0.0;
		LReturn ref =  orig.getBlockages().get(0);
		int zeros = 0;
		ArrayList<Integer> test = new ArrayList<Integer>();
		for (LReturn l : orig.getBlockages()) {
			if(ref!=l) {
				cosRule = ((ref.getEndDist()*ref.getEndDist())+(l.getStartDist()*l.getStartDist()))-2*(ref.getEndDist()*l.getStartDist()*Math.cos(Math.toRadians(Math.abs(ref.getEnd()-l.getStart()))));
				cosRule = Math.sqrt(cosRule);
				if (cosRule < theAgent.getSize()) {
					zeros = l.getStart() - ref.getEnd()-1;
					while(zeros > 0) {
						test.add(0);
						zeros--;
					}
					if (test.size() > 0) {
						ref.appendBlocks(test);
						test.clear();
					}
					ref.appendBlocks(l.getBlocks());
					System.out.println(ref.getBlocks());
					ref.setEnd(l.getEnd());
					changed = true;
					zeros = 0;
				}
				else {
					r.addBlockage(ref);
					ref = l;
				}
				cosRule = 0.0;
			}
		}
		//Cosrule for last item
		int angle = (orig.getBlockages().get(0).getStart() + 360) - orig.getBlockages().get(orig.getBlockages().size()-1).getEnd(); //Difference in angles
		//if cosrule using angle and the start/end dist of first and last item is less, then merge those items		
		if (changed == false) {
			r = orig;
		}
		else {
			r.addBlockage(ref);
		}
		return r;
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
	 * @param retSet - the Map
	 * @return the ResultSet of blockages
	 */
	public ReturnSet scanEnvironment(ReturnSet retSet) {
		Integer [] workingSet = theSensor.senseNext();
		Integer prev = 0;
		Integer temp = 0;
		CartesianPair agentOffset = new CartesianPair(theAgent.getPosition());
		Integer offSetAngle = theAgent.getPosition().getAngle();
		LReturn blockage = new LReturn();
		for (Integer i: workingSet) {
			if (i > 0) {
				if (blockage.getStart() == null) {
					blockage.setStart((temp + offSetAngle)%360);
				}
				else if (Math.abs(i - prev) > theAgent.getSize()) {
					blockage.setEnd(((temp -1) + offSetAngle)%360);
					retSet.addBlockage(blockage);
					blockage = new LReturn();
					blockage.setStart((temp + offSetAngle)%360);
				}
				CartesianPair currDist = new CartesianPair(new Waypoint((temp + offSetAngle)%360, i));
				currDist.setX(agentOffset.getX() + currDist.getX());
				currDist.setY(agentOffset.getY() + currDist.getY());
				Waypoint fin = new Waypoint(currDist);
				blockage.setDistance(fin.getDistance());
				if (temp == workingSet.length-1) {
					blockage.setEnd((temp + offSetAngle)%360);
					retSet.addBlockage(blockage);
				}
			}
			else if (blockage.getStart() != null) {
				blockage.setEnd(((temp -1) + offSetAngle)%360);
				retSet.addBlockage(blockage);
				blockage = new LReturn();
			}
			prev = i;
			temp++;
		}
		return retSet;
	}

	/**
	 * Amalgamates the blocks in a ReturnSet if there are any spans between objects smaller
	 * than the size of our agent. As explained by the bollard problem, it is possible for
	 * the relationship between consecutive blocks to fool the pathfinder into believing there
	 * is a valis gap when indeed there is not. Consider if a person is walking between two
	 * bollards. Three blockages are created. If the person is far back enough, we would
	 * originally percieve a gap here. By assessing each blockage in turn, we eliminate this
	 * problem.
	 * 
	 * @param original - the original ReturnSet generated by the scanEnvironment method
	 * @return a new returnSet with applicable objects amalgamated
	 */
	//	public ReturnSet blockageAmalgamation(ReturnSet original) {
	//		ReturnSet res = new ReturnSet();
	//		res = original;
	//		if (res.getBlockages().size() > 1) {
	//			res = merge(res, 0);
	//			for(int i = 1; i > res.getBlockages().size(); i++) {
	//				if (i < res.getBlockages().size()) {
	//					res = merge(res, i);
	//				}
	//			}
	//		}
	//		return res;
	//	}

	/**
	 * This is where the merging magic happens. It is a factorial operation by nature,
	 * we have to consider all of the other blockages in the returnSet to check if there
	 * is a valid gap.
	 * 
	 * This works by using the cosine rule from trigonometry to calculate the distance
	 * between the end of our considered block and the next n blockage. We know the angle
	 * and 2 of the distances, so we calculate the third side. If this value is larger than
	 * our agent size, we merge the block and loop.
	 * 
	 * If the blockage was larger than our agent size, we put it in a scratchpad. We do this
	 * so that if we ever encounter a smaller gap than our agent later, we can insert this
	 * data and not lose accuracy.
	 * 
	 * 0's are inserted if there were any gaps between blockages.
	 * 
	 * @param orig the original version of the res returnSet
	 * @param counter the location in the returnSet we are considering
	 * @return The returnSet with any merges for the considered item applied
	 */


	//	public ReturnSet merge (ReturnSet orig, int counter) {
	//		ReturnSet temp = new ReturnSet();
	//		ReturnSet scratch = new ReturnSet();
	//		double cosRule;
	//		int zeros = 0;
	//		int pos;
	//		LReturn prev = new LReturn();
	//		prev = orig.getBlockages().get(counter);
	//		for(LReturn l : orig.getBlockages()) {
	//			if (orig.getBlockages().indexOf(l) > orig.getBlockages().indexOf(prev)) {
	//				cosRule = ((prev.getEndDist()*prev.getEndDist())+(l.getStartDist()*l.getStartDist()))-2*(prev.getEndDist()*l.getStartDist()*Math.cos(Math.toRadians(Math.abs(prev.getEnd()-l.getStart()))));
	//				cosRule = Math.sqrt(cosRule);
	//				pos = orig.getBlockages().indexOf(l);
	//				pos--;
	//				zeros = Math.abs(orig.getBlockages().get(pos).getEnd() - l.getStart())-1;
	//				while(zeros > 0) {
	//					l.insertZero();
	//					zeros--;
	//				}
	//				zeros = 0;
	//				if (cosRule >= theAgent.getSize()) {
	//					scratch.addBlockage(l);
	//				}
	//				else {
	//					for(LReturn m : scratch.getBlockages()) {
	//						prev.appendBlocks(m.getBlocks());
	//					}
	//					prev.appendBlocks(l.getBlocks());
	//					prev.setEnd(l.getEnd());
	//					scratch.removeAll();
	//				}
	//				cosRule = 0;
	//			}
	//		}
	//		if(temp.getBlockages().isEmpty()) {
	//			temp.addBlockage(prev);
	//		}
	//		for(LReturn n: scratch.getBlockages()) {
	//			temp.addBlockage(n);
	//		}
	//		return temp;	
	//	}
}


