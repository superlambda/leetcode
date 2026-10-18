package peter.liu.reversebits;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SolutionTest {

	@Test
	void test() {
		Solution solution = new Solution();
		assertEquals(964176192, solution.reverseBits(43261596));
		assertEquals(-1073741825, solution.reverseBits(-3));
		
		
	}

}
