package lidarMapping;

import org.junit.Test;
import static org.junit.Assert.*;

public class UnitTesting {

	@Test
	public void LReturnBlankConstructorTest() {
		LReturn testRet = new LReturn();
		assertTrue(testRet.getStart() == null);
		testRet.setStart(1);
		assertTrue(testRet.getStart() == 1);
		assertTrue(testRet.getEnd() == null);
		testRet.setEnd(21);
		assertTrue(testRet.getEnd() == 21);
		for(int i = 0; i >360; i++) {
			testRet.setDistance(i + 360);
			assertTrue(testRet.getDistance(i) == i+ 360);
		}
	}


}
