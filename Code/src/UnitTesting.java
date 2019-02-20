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
		//TODO Check that anti and clockwise are correct - circle angles are odd
		
		b.addBlockage(testBlock3);
		b.addBlockage(testBlock4);
		assertTrue(b.getBlockages().get(0).getStart() == 89);
		assertTrue(b.getBlockages().get(0).getStartDist() == 40);
		assertTrue(b.getBlockages().get(1).getEnd() == 100);
		assertTrue(b.getBlockages().get(1).getEndDist() == 129);
	}
}
