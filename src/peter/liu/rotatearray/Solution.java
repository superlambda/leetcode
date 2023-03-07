package peter.liu.rotatearray;

/**
 * Rotate an array of n elements to the right by k steps.
 * 
 * For example, with n = 7 and k = 3, the array [1,2,3,4,5,6,7] is rotated to
 * [5,6,7,1,2,3,4].
 * 
 * Note: Try to come up as many solutions as you can, there are at least 3
 * different ways to solve this problem.
 * 
 * [show hint]
 * 
 * Related problem: Reverse Words in a String II
 * 
 * Credits: Special thanks to @Freezen for adding this problem and creating all
 * test cases.
 * 
 * @author pcnsh197
 *
 */
public class Solution {
//	public void rotate(int[] nums, int k) {
//		k %= nums.length;
//		if (k == 0) {
//			return;
//		}
//		int[] temp = new int[k];
//		for (int i = 0; i < k; i++) {
//			temp[i] = nums[nums.length - k + i];
//		}
//		for (int i = nums.length - 1; i - k >= 0; i--) {
//			nums[i] = nums[i - k];
//		}
//		for (int i = 0; i < k; i++) {
//			nums[i] = temp[i];
//		}
//	}
//
//	/**
//	 * O(1) capacity solution
//	 * 
//	 * @param nums
//	 * @param k
//	 */
	public void rotate1(int[] nums, int k) {
		if (nums.length == 0) {
			return;
		}
		int n = nums.length;
		while ((k %= n) > 0 && n > 1) {
			int range = n - k;
			for (int i = 1; i <= range; i++) {
				int val = nums[n - i];
				nums[n - i] = nums[n - i - k];
				nums[n - i - k] = val;
			}
			n = k;
			k = n - (range % k);
		}
	}

	public void rotate(int[] nums, int k) {

		if (nums == null || nums.length == 0) {
			return;
		}
		k = k % nums.length;
		if(k==0) {
			return;
		}
		reverse(nums, 0, nums.length-1);
		reverse(nums, 0, k-1);
		reverse(nums, k, nums.length-1);
	}
	
	public void reverse(int[] nums, int start, int end) {
		int temp;
		while(start < end) {
			temp = nums[start];
			nums[start] = nums[end];
			nums[end] = temp;
			start++;
			end--;
		}
	}

}
