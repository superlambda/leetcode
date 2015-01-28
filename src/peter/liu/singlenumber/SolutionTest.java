package peter.liu.singlenumber;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testSingleNumber() {
		int[] a={3,4,5,4,5,3,7,8,8};
		assertTrue(new Solution().singleNumber(a)==7);
	}

}
