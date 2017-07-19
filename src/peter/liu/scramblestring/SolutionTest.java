package peter.liu.scramblestring;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void test() {
		assertTrue(new Solution().isScramble("abc", "bca"));
	}

}
