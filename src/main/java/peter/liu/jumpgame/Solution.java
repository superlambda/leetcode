package peter.liu.jumpgame;

/**
 * Given an array of non-negative integers, you are initially positioned at the
 * first index of the array.
 * 
 * Each element in the array represents your maximum jump length at that
 * position.
 * 
 * Determine if you are able to reach the last index.
 * 
 * For example: A = [2,3,1,1,4], return true.
 * 
 * A = [3,2,1,0,4], return false.
 * 
 * @author superlambda
 *
 */
public class Solution {
	public boolean canJump(int[] nums) {
		if (nums.length <= 1) {
			return true;
		}
		int zeroNumber = 0;
		int index = 0;
		while (index < nums.length) {
			if (nums[index] == 0) {
				int startIndex = index - 1;
				zeroNumber = 0;
				while (index < nums.length && nums[index] == 0) {
					zeroNumber++;
					index++;
				}
				boolean indexFound = false;
				int numberToSubstract = index == nums.length ? 1 : 0;
				numberToSubstract = zeroNumber - numberToSubstract + startIndex;
				while (startIndex >= 0) {
					if (nums[startIndex] > numberToSubstract - startIndex) {
						indexFound = true;
						break;
					}
					startIndex--;
				}
				if (!indexFound) {
					return false;
				}
			}
			index++;
		}
		return true;
	}
}
