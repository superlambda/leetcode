package peter.liu.subarraysumequalsk;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SolutionTest {

	@Test
	void test() {
//		int [] num = {1,1,1};
//		assertEquals(2, new Solution().subarraySum(num, 2));
		int [] num = {2,-2,3,0,4,-7};
		assertEquals(4, new Solution().subarraySum(num, 0));
	}

}
