package peter.liu.removeduplicatesfromsortedarrayII;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testRemoveDuplicates() {
		int[] nums = new int[] { 1, 1, 1, 2, 2, 3 };
		int length = new Solution().removeDuplicates(nums);
		assertEquals(length, 5);
		for (int num : nums) {
			System.out.print(num + " ");

		}
		System.out.println(" ");
		int[] nums2 = new int[] { 1, 1, 1, 1, 3, 3 };

		int length2 = new Solution().removeDuplicates(nums2);
		assertEquals(length2, 4);
		for (int num : nums2) {
			System.out.print(num + " ");
		}
		System.out.println(" ");

	}

}
