package peter.liu.compareversionnumber;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SolutionTest {

	@Test
	public void testCompareVersion() {
		Solution s=new Solution();
		assertTrue(s.compareVersion("1", "0")==1);
		assertTrue(s.compareVersion("1.0", "1.1")==-1);
		assertTrue(s.compareVersion("1", "1.1")==-1);
	}

}
