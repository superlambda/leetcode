package peter.liu.searchinrotatedsortedarray;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SolutionTest {

	@Test
	public void testSearch() {
		int[] nums = new int[] { 1, 2, 3, 4, 5, 6 };
		assertEquals(new Solution().search(nums, 4), 3);

	}

}
