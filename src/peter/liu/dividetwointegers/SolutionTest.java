package peter.liu.dividetwointegers;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testDivide() {
		
		System.out.println(Math.pow(2, 30));
		assertEquals(6, new Solution().divide(19, 3));
		assertEquals(2147483647, new Solution().divide(-2147483648, -1));
		assertEquals(-1073741824, new Solution().divide(-2147483648, 2));
		
	}

}
