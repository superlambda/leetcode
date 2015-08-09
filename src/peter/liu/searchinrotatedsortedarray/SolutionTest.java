package peter.liu.searchinrotatedsortedarray;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testSearch() {
		int[] nums = new int[] { 1, 2, 3, 4, 5, 6 };
		assertEquals(new Solution().search(nums, 4), 3);

	}

}
