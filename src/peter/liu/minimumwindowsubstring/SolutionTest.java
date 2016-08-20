package peter.liu.minimumwindowsubstring;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testMinWindow() {
		assertEquals(new Solution().minWindow("ADOBECODEBANC", "ABC"), "BANC");
		assertEquals(new Solution().minWindow("a", "a"), "a");
		assertEquals(new Solution().minWindow("ab", "b"), "b");
		
		assertEquals(new Solution().minWindow("abc", "b"), "b");
	}

}
