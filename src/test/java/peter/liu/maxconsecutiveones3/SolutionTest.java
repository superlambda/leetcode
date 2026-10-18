package peter.liu.maxconsecutiveones3;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SolutionTest {

	@Test
	public void testLongestOnes() {
		int[] nums = { 1, 0, 0, 1, 1, 1 };
		assertEquals(4, new Solution().longestOnes(nums, 1));
	}

}
