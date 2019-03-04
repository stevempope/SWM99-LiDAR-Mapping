package lidarMapping;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit testing for all classes in the package.
 * 
 * Rigourous method by method testing needs to be conducted to demonstrate the function
 * of my model.
 * 
 * @author Stephen Pope 15836791
 * @version 0.1
 */

import java.util.ArrayList;

public class UnitTesting {

	/*
	 * LReturn Suite
	 */

	@Test
	public void LReturnBlankConstructorTest() {
		LReturn testRet = new LReturn();
		assertTrue(testRet.getStart() == null);
		testRet.setStart(1);
		assertTrue(testRet.getStart() == 1);
		assertTrue(testRet.getEnd() == null);
		testRet.setEnd(21);
		assertTrue(testRet.getEnd() == 21);
		for(int i = 0; i >20; i++) {
			testRet.setDistance(i + 360);
			assertTrue(testRet.getDistance(i) == i+ 360);
		}
	}

	@Test
	public void LReturnRegularConstructorTest() {
		ArrayList<Integer> blocks = new ArrayList<Integer>();
		for (int i = 0; i > 179; i++) {
			blocks.add(100+i);
		}
		LReturn testRet = new LReturn(180,blocks,359);
		assertTrue(testRet.getStart() == 180);
		assertTrue(testRet.getEnd() ==359);
		for (int i = 0; i > 179; i++) {
			assertTrue(testRet.getDistance(i) == 100 + i);
		}
	}

	@Test
	public void LReturnMethodTests() {
		LReturn testRet = new LReturn();

		testRet.setStart(4);
		assertTrue(testRet.getStart() == 4);

		testRet.setEnd(10);
		assertTrue(testRet.getEnd() == 10);

		testRet.setDistance(250);
		testRet.setDistance(645930);
		assertTrue(testRet.getDistance(0) == 250);
		assertTrue(testRet.getDistance(1) == 645930);

		testRet.setStartScore(5);
		assertTrue(testRet.getStartScore() == 5);
		//FIXME Method needs to accept doubles for Pathfinding later on

		testRet.setEndScore(1);
		assertTrue(testRet.getEndScore() == 1);
		//FIXME Method needs to accept doubles for Pathfinding later on

		assertTrue(testRet.getStartDist() == 250);
		assertTrue(testRet.getEndDist() == 645930);
	}
	
	/*
	 * ReturnSet Suite
	 */
	
	@Test
	public void ReturnSetTest() {
		ArrayList<Integer> blocks = new ArrayList<Integer>();
		for (int i = 0; i < 90; i++) {
			blocks.add(i + 40);
		}
		LReturn testBlock1 = new LReturn(0,blocks,89);
		LReturn testBlock2 = new LReturn(100, blocks, 189);
		
		ReturnSet a = new ReturnSet();
		assertTrue(a.getOrientation() == Orientation.antiClockwise);
		
		a.addBlockage(testBlock1);
		a.addBlockage(testBlock2);
		assertTrue(a.getBlockages().get(0).getStart() == 0);
		assertTrue(a.getBlockages().get(0).getStartDist() == 40);
		assertTrue(a.getBlockages().get(1).getEnd() == 189);
		assertTrue(a.getBlockages().get(1).getEndDist() == 129);
		
		
		ReturnSet b = new ReturnSet(Orientation.clockwise);
		
		LReturn testBlock3 = new LReturn(89, blocks, 0);
		LReturn testBlock4 = new LReturn(189, blocks, 100);
		
		b.addBlockage(testBlock3);
		b.addBlockage(testBlock4);
		assertTrue(b.getBlockages().get(0).getStart() == 89);
		assertTrue(b.getBlockages().get(0).getStartDist() == 40);
		assertTrue(b.getBlockages().get(1).getEnd() == 100);
		assertTrue(b.getBlockages().get(1).getEndDist() == 129);
	}
	
	/*
	 * VLSensor Suite
	 */
	
	@Test
	public void VLSensorTest() {
		VLsensor testSensor1 = new VLsensor();
		VLsensor testSensor2 = new VLsensor(Orientation.clockwise);
		
		assertTrue(testSensor1.getOrientation() == Orientation.antiClockwise);
		assertTrue(testSensor1.sense(0).length == 360);
		assertTrue(testSensor1.sense(1).length == 360);
		assertTrue(testSensor1.sense(2).length == 360);
		
		assertTrue(testSensor2.getOrientation() == Orientation.clockwise);
		assertTrue(testSensor2.sense(0).length == 360);
		assertTrue(testSensor2.sense(1).length == 360);
		assertTrue(testSensor2.sense(2).length == 360);
	}
	
	/*
	 * Map Suite
	 */
	
	@Test
	public void MapTest() {
		ArrayList<Integer> blocks = new ArrayList<Integer>();
		for (int i = 0; i < 90; i++) {
			blocks.add(i + 40);
		}
		LReturn return1 = new LReturn(15, blocks, 105);
		LReturn return2 = new LReturn(150, blocks, 240);
		LReturn return3 = new LReturn(258, blocks, 348);
		LReturn return4 = new LReturn(9, blocks, 99);
		ReturnSet rs1 = new ReturnSet();
		rs1.addBlockage(return1);
		rs1.addBlockage(return2);
		rs1.addBlockage(return3);
		ReturnSet rs2 = new ReturnSet();
		rs2.addBlockage(return4);
		Map theMap = new Map();
		theMap.addScan(rs1);
		theMap.addScan(rs2);
		assertTrue(theMap.getBlockages().size() == 2);
		assertTrue(theMap.getBlockages().get(0).getOrientation() == Orientation.antiClockwise);
		theMap.clearMap();
		assertTrue(theMap.getBlockages().size() == 0);
	}
	
	/*
	 * Waypoint Suite
	 */
	
	@Test
	public void WaypointTest(){
		Waypoint wp0 = new Waypoint();
		assertTrue(wp0.getAngle() == null);
		assertTrue(wp0.getDistance() == null);
		wp0.setAngle(0);
		wp0.setDistance(112910);
		assertTrue(wp0.getAngle() == 0);
		assertTrue(wp0.getDistance() == 112910);
		
		Waypoint wp1 = new Waypoint (5, 10000);
		assertTrue(wp1.getAngle() == 5);
		assertTrue(wp1.getDistance() == 10000);
	}
	
	/*
	 * Path Suite
	 */
	
	@Test
	public void PathTest() {
		Waypoint wp0 = new Waypoint(1,1);
		Waypoint wp1 = new Waypoint(1,100);
		Waypoint wp2 = new Waypoint(84,200); 
		Path testPath = new Path();
		testPath.addWaypoint(wp0);
		testPath.addWaypoint(wp1);
		testPath.addWaypoint(wp2);
		testPath.insertIntoPath(1, wp0);
		
		assertTrue(testPath.getPath().size() == 4);
		assertTrue(testPath.popNextWaypoint() == wp0);
		assertTrue(testPath.getPath().size() == 3);
		testPath.removePathWaypoint(2);
		assertTrue(testPath.getPath().size() ==2);
		testPath.clearPath();
		assertTrue(testPath.popNextWaypoint() == null);
		assertTrue(testPath.getPath().size() == 0);
		
	}
	
	/*
	 * Processor Suite
	 */
	
	@Test
	public void ProcessorTest() {
		VLsensor sensor = new VLsensor();
		Integer agentSize = 10;
		Map theMap = new Map();
		Processor tp = new Processor(sensor, agentSize);
		
		tp.updateMap(theMap);
		
		assertTrue(theMap.getBlockages().get(0).getBlockages().size() == 3);
		assertTrue(theMap.getBlockages().get(0).getBlockages().get(0).getStart() == 1);
		assertTrue(theMap.getBlockages().get(0).getBlockages().get(0).getStartDist() == 11);
		assertTrue(theMap.getBlockages().get(0).getBlockages().get(0).getEnd() == 21);
		assertTrue(theMap.getBlockages().get(0).getBlockages().get(0).getEndDist() == 21);
		
		assertTrue(theMap.getBlockages().get(0).getBlockages().get(1).getStart() == 22);
		assertTrue(theMap.getBlockages().get(0).getBlockages().get(1).getStartDist() == 99);
		assertTrue(theMap.getBlockages().get(0).getBlockages().get(1).getEnd() == 22);
		assertTrue(theMap.getBlockages().get(0).getBlockages().get(1).getEndDist() == 99);
		
		assertTrue(theMap.getBlockages().get(0).getBlockages().get(2).getStart() == 23);
		assertTrue(theMap.getBlockages().get(0).getBlockages().get(2).getStartDist() == 23);
		assertTrue(theMap.getBlockages().get(0).getBlockages().get(2).getEnd() == 358);
		assertTrue(theMap.getBlockages().get(0).getBlockages().get(2).getEndDist() == 358);
		assertTrue(theMap.getBlockages().get(0).getBlockages().get(2).getDistance(4) == 27);
		
		tp.updateMap(theMap);
		
		assertTrue(theMap.getBlockages().get(1).getBlockages().get(0).getStart() == 0);
		assertTrue(theMap.getBlockages().get(1).getBlockages().get(0).getStartDist() == 100);
		assertTrue(theMap.getBlockages().get(1).getBlockages().get(0).getEnd() == 359);
		assertTrue(theMap.getBlockages().get(1).getBlockages().get(0).getEndDist() == 100);
		
		tp.updateMap(theMap);
		
		assertTrue(theMap.getBlockages().get(2).getBlockages().size() == 7);
		
		sensor = new VLsensor(Orientation.clockwise);
		tp =  new Processor(sensor,agentSize);
		
		tp.updateMap(theMap);
		
		assertTrue(theMap.getBlockages().get(3).getBlockages().size() == 4);
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(0).getStart() == 0);
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(0).getStartDist() == 11);
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(0).getEnd() == 4);
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(0).getEndDist() == 4);
		
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(1).getStart() == 5);
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(1).getStartDist() == 1000);
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(1).getEnd() == 5);
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(1).getEndDist() == 1000);
		
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(2).getStart() == 6);
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(2).getStartDist() == 6);
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(2).getEnd() == 358);
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(2).getEndDist() == 358);
		
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(3).getStart() == 359);
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(3).getStartDist() == 300);
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(3).getEnd() == 359);
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(3).getEndDist() == 300);
		
		tp.updateMap(theMap);
		
		assertTrue(theMap.getBlockages().get(4).getBlockages().size() == 1);
		assertTrue(theMap.getBlockages().get(4).getBlockages().get(0).getStart() == 0);
		assertTrue(theMap.getBlockages().get(4).getBlockages().get(0).getStartDist() == 100);
		assertTrue(theMap.getBlockages().get(4).getBlockages().get(0).getEnd() == 359);
		assertTrue(theMap.getBlockages().get(4).getBlockages().get(0).getEndDist() == 100);
		
		tp.updateMap(theMap);
		
		assertTrue(theMap.getBlockages().get(5).getBlockages().size() == 7);
		
		sensor = new VLsensor(Orientation.antiClockwise);
		theMap = new Map();
		tp = new Processor(sensor, agentSize);
		
		tp.smarterUpdateMap(theMap);
		
		assertTrue(theMap.getBlockages().get(0).getBlockages().size() == 2);
		assertTrue(theMap.getBlockages().get(0).getBlockages().get(0).getEnd() == 358);
		assertTrue(theMap.getBlockages().get(0).getBlockages().get(0).getEndDist() == 358);
		
		tp.smarterUpdateMap(theMap);
		
		assertTrue(theMap.getBlockages().get(1).getBlockages().size() == 1);
		
		tp.smarterUpdateMap(theMap);
		
		assertTrue(theMap.getBlockages().get(2).getBlockages().size() == 7);
		
		System.out.printf("tests complete \n");
		tp.smarterUpdateMap(theMap);
		assertTrue(theMap.getBlockages().get(3).getBlockages().size() == 1);
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(0).getStartDist() == 50);
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(0).getDistance(1) == 0);
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(0).getStart() == 0);
		assertTrue(theMap.getBlockages().get(3).getBlockages().get(0).getEnd() == 358);
	}
	
	
	/*
	 * Pathfinder Suite
	 */
	
	@Test
	public void pathfindingTest() {
		VLsensor sensor = new VLsensor(Orientation.antiClockwise);
		Integer agentSize = 10;
		Map theMap = new Map();
		Processor tp = new Processor(sensor, agentSize);
		Pathfinder pf = new Pathfinder();
		Waypoint dest = new Waypoint(300, 200);
		Path thePath;
		
		tp.smarterUpdateMap(theMap);
		thePath = pf.pathfind(theMap, dest);
		assertTrue(thePath.getPath().size() == 1);
		assertTrue(thePath.getPath().get(0) == dest);
		
		
		dest.setAngle(15);
		dest.setDistance(400);
		thePath = pf.pathfind(theMap, dest);
		assertTrue(thePath.getPath().size() == 2);
		assertTrue(thePath.getPath().get(0).getAngle() == 0);
		assertTrue(thePath.getPath().get(0).getDistance() == 11);
		assertTrue(thePath.getPath().get(1) == dest);
		
		theMap.clearMap();
		tp.smarterUpdateMap(theMap);
		dest.setDistance(10000);
		thePath = pf.pathfind(theMap, dest);
		assertTrue(thePath.getPath().size() == 2);
	}
	
	
}
