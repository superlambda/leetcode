package peter.liu.sortcolors;

/**
 * Given an array with n objects colored red, white or blue, sort them so that
 * objects of the same color are adjacent, with the colors in the order red,
 * white and blue.
 * 
 * Here, we will use the integers 0, 1, and 2 to represent the color red, white,
 * and blue respectively.
 * 
 * Note: You are not suppose to use the library's sort function for this
 * problem.
 * 
 * @author superlambda
 *
 */
public class Solution {

	public void sortColors(int[] nums) {
		int numberOfZero = 0;
		int numberOfOne = 0;
		for (int number : nums) {
			if (number == 0) {
				numberOfZero++;
			} else if (number == 1) {
				numberOfOne++;
			}
		}

		for (int i = 0; i < nums.length; i++) {
			if (numberOfZero > 0) {
				nums[i] = 0;
				numberOfZero--;
			} else if (numberOfOne > 0) {
				nums[i] = 1;
				numberOfOne--;
			} else {
				nums[i] = 2;
			}
		}

	}
}
