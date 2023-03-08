package peter.liu.hammingdistance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SolutionTest {

	@Test
	void test() {
		Solution solution = new Solution();
		assertEquals(2, solution.hammingDistance(1, 4));
		assertEquals(1, solution.hammingDistance(3, 1));
	}

}
