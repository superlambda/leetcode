package peter.liu.mergesortedarray;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class SolutionTest {

	@Test
	public void testTwoNotEmptyArray() {

		int[] nums1 = { 1, 2, 3, 0, 0, 0 };
		int[] nums2 = { 2, 5, 6 };
		new Solution().merge(nums1, 3, nums2, 3);
		int[] checkArray = { 1, 2, 2, 3, 5, 6 };
		assertArrayEquals(checkArray, nums1);

	}

	@Test
	public void testSecondArrayIsEmpty() {

		int[] nums1 = { 1 };
		int[] nums2 = {};
		new Solution().merge(nums1, 1, nums2, 0);
		int[] checkArray = { 1 };
		assertArrayEquals(checkArray, nums1);

	}

	@Test
	public void testFirstArrayIsEmpty() {

		int[] nums1 = { 0 };
		int[] nums2 = { 1 };
		new Solution().merge(nums1, 0, nums2, 1);
		int[] checkArray = { 1 };
		assertArrayEquals(checkArray, nums1);

	}

}
