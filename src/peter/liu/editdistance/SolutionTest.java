package peter.liu.editdistance;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testMinDistance() {
		assertEquals(1, new Solution().minDistance("a", "b"));
		assertEquals(2, new Solution().minDistance("ab", "bc"));
		
		assertEquals(3, new Solution().minDistance("horse", "ros"));
		
		
		assertEquals(6, new Solution().minDistance("plasma", "altruism"));
	}

}
