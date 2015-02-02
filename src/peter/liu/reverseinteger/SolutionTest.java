package peter.liu.reverseinteger;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testReverse() {
		System.out.println(Integer.MAX_VALUE);
		
		assertEquals(new Solution().reverse(123),321);
		assertEquals(new Solution().reverse(-123),-321);
		assertEquals(new Solution().reverse(1534236469),0);
	}

}
