package peter.liu.scramblestring;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SolutionTest {

	@Test
	public void test() {
		assertTrue(new Solution().isScramble("abc", "bca"));
	}

}
