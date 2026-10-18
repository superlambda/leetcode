package peter.liu.climbingstairs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


class SolutionTest {

	@Test
	void test() {
		assertEquals(1, new Solution().climbStairs(1));
		assertEquals(2, new Solution().climbStairs(2));
		assertEquals(3, new Solution().climbStairs(3));
		assertEquals(5, new Solution().climbStairs(4));
		assertEquals(8, new Solution().climbStairs(5));
	}

}
