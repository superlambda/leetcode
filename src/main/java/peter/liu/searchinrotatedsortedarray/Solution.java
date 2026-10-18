package peter.liu.searchinrotatedsortedarray;

/**
 * Suppose a sorted array is rotated at some pivot unknown to you beforehand.
 * 
 * (i.e., 0 1 2 4 5 6 7 might become 4 5 6 7 0 1 2).
 * 
 * You are given a target value to search. If found in the array return its
 * index, otherwise return -1.
 * 
 * You may assume no duplicate exists in the array.
 * 
 * @author superlambda
 *
 */
public class Solution {

	public int search(int[] nums, int target) {
		int index = 0;
		for (int i = 0; i < nums.length - 1; i++) {
			if (nums[i + 1] < nums[i]) {
				index = i + 1;
				break;
			}
		}
		int low, high;
		if (nums[index] == target) {
			return index;
		} else if (nums[index] < target && nums[nums.length - 1] >= target) {
			low = index;
			high = nums.length - 1;
		} else if (nums[0] <= target && index>0&&nums[index-1] >= target) {
			low = 0;
			high = index - 1;
		} else {
			return -1;
		}
		while (low <= high) {
			int mid = (high - low) / 2 + low;
			if (nums[mid] == target) {
				return mid;
			} else if (nums[mid] < target) {
				low = mid + 1;
			} else {
				high = mid - 1;
			}
		}
		return -1;

	}
}
