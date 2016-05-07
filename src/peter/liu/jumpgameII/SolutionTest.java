package peter.liu.jumpgameII;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testJump() {
		int[] nums = { 2, 3, 1, 1, 4 };
		assertEquals(new Solution().jump(nums), 2);
//		int[] nums2 = {100,99,98,97,96,95,94,93,92,91,90,89,88,87,86,85,84,83,82,81,80,79,78,77,76,75,74,73,72,71,70,69,68,67,66,65,64,63,62,61,60,59,58,57,56,55,54,53,52,51,50,49,48,47,46,45,44,43,42,41,40,39,38,37,36,35,34,33,32,31,30,29,28,27,26,25,24,23,22,21,20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,1,0};
//		System.out.println(new Solution().jump(nums2));
//		
//		int[] nums3 ={5,8,1,8,9,8,7,1,7,5,8,6,5,4,7,3,9,9,0,6,6,3,4,8,0,5,8,9,5,3,7,2,1,8,2,3,8,9,4,7,6,2,5,2,8,2,7,9,3,7,6,9,2,0,8,2,7,8,4,4,1,1,6,4,1,0,7,2,0,3,9,8,7,7,0,6,9,9,7,3,6,3,4,8,6,4,3,3,2,7,8,5,8,6,0};
//		System.out.println(new Solution().jump(nums3));
		int[] nums4={5,6,4,4,6,9,4,4,7,4,4,8,2,6,8,1,5,9,6,5,2,7,9,7,9,6,9,4,1,6,8,8,4,4,2,0,3,8,5};
		assertEquals(new Solution().jump(nums4),5);
		int[] nums5= {3,4,3,1,0,7,0,3,0,2,0,3};
		assertEquals(new Solution().jump(nums5), 3);
		
		int[] nums6={5,4,0,1,3,6,8,0,9,4,9,1,8,7,4,8};
		assertEquals(new Solution().jump(nums6), 3);
	}

}
