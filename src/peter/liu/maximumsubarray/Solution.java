package peter.liu.maximumsubarray;

/**
 * Find the contiguous subarray within an array (containing at least one number)
 * which has the largest sum.
 * 
 * For example, given the array [−2,1,−3,4,−1,2,1,−5,4], the contiguous subarray
 * [4,−1,2,1] has the largest sum = 6.
 * 
 * @author superlambda
 *
 */
public class Solution {
	public int maxSubArray(int[] nums) {
		int max = Integer.MIN_VALUE, sum = 0;
		for (int i = 0; i < nums.length; i++) {
			if (sum < 0) {
				sum = nums[i];
			} else {
				sum += nums[i];
			}
			if (sum > max) {
				max = sum;
			}
		}
		return max;
		// int sum = Integer.MIN_VALUE;
		// int trySum = nums[0];
		// for (int i = 1; i < nums.length; i++) {
		// int temp = trySum + nums[i];
		// if (temp < trySum) {
		// if (sum < trySum) {
		// sum = Math.max(trySum, nums[i]);
		// trySum = nums[i];
		// }
		// } else {
		// trySum = Math.max(temp, nums[i]);
		// }
		//
		// }
		// return trySum > sum ? trySum : sum;
	}

}
