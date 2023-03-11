package inverview.tesla.task3;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

class SolutionTest {

	@Test
	void test() {
		
		int[] A = {2,-2,3,0,4,-7};
		assertEquals(new Solution().solution(A),4);
	}

}
