package peter.liu.intersectionoftwoarraysII;

import org.junit.jupiter.api.Test;

class SolutionTest {

	@Test
	void test() {
		int[] nums1 = {4,7,9,7,6,7};
		int[] nums2 = {5,0,0,6,1,6,2,2,4};
		int[] result = new Solution().intersect(nums1, nums2);
		for(int num: result) {
			System.out.print(num+",");
		}
	}

}
