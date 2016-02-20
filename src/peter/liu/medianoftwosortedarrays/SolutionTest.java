package peter.liu.medianoftwosortedarrays;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testFindMedianSortedArrays() {
		int[] nums1 = { 1,2,2};
		int[] nums2 = { 1,2,3 };
		System.out.println(new Solution().findMedianSortedArrays(nums1, nums2));
		
		
	}

}
