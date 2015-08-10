package peter.liu.searchinrotatedsortedarrayII;

/**
 * Follow up for "Search in Rotated Sorted Array": What if duplicates are
 * allowed?
 * 
 * Would this affect the run-time complexity? How and why?
 * 
 * Write a function to determine if a given target is in the array.
 * 
 * @author superlambda
 *
 */
public class Solution {

	//It's not the bese solution, it is O(n), the best is o(log n).
	public boolean search(int[] nums, int target) {
		int index = 0;
		for (int i = 0; i < nums.length - 1; i++) {
			if (nums[i + 1] < nums[i]) {
				index = i + 1;
				break;
			}
		}
		int low, high;
		if (nums[index] == target) {
			return true;
		} else if (nums[index] < target && nums[nums.length - 1] >= target) {
			low = index;
			high = nums.length - 1;
		} else if (nums[0] <= target && index > 0 && nums[index - 1] >= target) {
			low = 0;
			high = index - 1;
		} else {
			return false;
		}
		while (low <= high) {
			int mid = (high - low) / 2 + low;
			if (nums[mid] == target) {
				return true;
			} else if (nums[mid] < target) {
				low = mid + 1;
			} else {
				high = mid - 1;
			}
		}
		return false;
	}
}
