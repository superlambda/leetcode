package peter.liu.removeduplicatesfromsortedarray;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author pcnsh197
 *
 */
public class SolutionTest {
	@Test
	public void testRemoveDuplicates() {
		int[] A = { 1, 1, 2 };
		assertEquals(new Solution().removeDuplicates(A), 2);
	}

}
