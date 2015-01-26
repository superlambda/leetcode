package peter.liu.compareversionnumber;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testCompareVersion() {
		Solution s=new Solution();
		assertTrue(s.compareVersion("1", "0")==1);
		assertTrue(s.compareVersion("1.0", "1.1")==-1);
		assertTrue(s.compareVersion("1", "1.1")==-1);
	}

}
