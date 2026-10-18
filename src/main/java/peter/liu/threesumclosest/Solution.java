package peter.liu.threesumclosest;

import java.util.Arrays;

/**
 * Given an array S of n integers, find three integers in S such that the sum is
 * closest to a given number, target. Return the sum of the three integers. You
 * may assume that each input would have exactly one solution.
 * 
 * For example, given array S = {-1 2 1 -4}, and target = 1.
 * 
 * The sum that is closest to the target is 2. (-1 + 2 + 1 = 2).
 * 
 * @author superlambda
 *
 */
public class Solution {
	public int threeSumClosest(int[] num, int target) {
		int result = 0;
		int difference = Integer.MAX_VALUE;
		Arrays.sort(num);
		for (int i = 0; i < num.length - 2; i++) {
			int low = i + 1;
			int high = num.length - 1;
			while (low < high) {
				if (num[i] + num[low] + num[high] == target) {
					return target;
				} else if (num[i] + num[low] + num[high] > target) {
					if (num[i] + num[low] + num[high] - target < difference) {
						difference = num[i] + num[low] + num[high] - target;
						result = num[i] + num[low] + num[high];
					}
					high--;
				} else {
					if (target - (num[i] + num[low] + num[high]) < difference) {
						difference = target - (num[i] + num[low] + num[high]);
						result = num[i] + num[low] + num[high];
					}
					low++;
				}
			}
		}
		return result;
	}
}
