package peter.liu.longestconsecutivesequence;

import java.util.Arrays;

/**
 * Given an unsorted array of integers, find the length of the longest
 * consecutive elements sequence.
 * 
 * For example, Given [100, 4, 200, 1, 3, 2], The longest consecutive elements
 * sequence is [1, 2, 3, 4]. Return its length: 4.
 * 
 * Your algorithm should run in O(n) complexity.
 * 
 * @author superlambda
 *
 */
public class Solution {
	public int longestConsecutive(int[] nums) {
		if(nums.length==1){
			return 1;
		}
		Arrays.sort(nums);
		int longest = 0;
		int candidate = 1;
		for (int i = 0; i < nums.length - 1; i++) {
			if (nums[i + 1] - nums[i] == 1) {
				candidate++;
			} else if(nums[i + 1] - nums[i] >1) {
				candidate = 1;
			}
			longest = candidate > longest ? candidate : longest;
		}
		return longest;
	}
}
