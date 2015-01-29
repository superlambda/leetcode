package peter.liu.singlenumberII;

import static org.junit.Assert.*;
import org.junit.Test;

public class SolutionTest {
	@Test
	public void testSingleNumber() {
//		int[] a={1};
//		assertTrue(new Solution().singleNumber(a)==1);
	 	int[] b={2,2,3,2};
	 	assertTrue(new Solution().singleNumber(b)==3);
	}

}
