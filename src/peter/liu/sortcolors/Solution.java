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

	// public void sortColors(int[] nums) {
	// int numberOfZero = 0;
	// int numberOfOne = 0;
	// for (int number : nums) {
	// if (number == 0) {
	// numberOfZero++;
	// } else if (number == 1) {
	// numberOfOne++;
	// }
	// }
	//
	// for (int i = 0; i < nums.length; i++) {
	// if (numberOfZero > 0) {
	// nums[i] = 0;
	// numberOfZero--;
	// } else if (numberOfOne > 0) {
	// nums[i] = 1;
	// numberOfOne--;
	// } else {
	// nums[i] = 2;
	// }
	// }
	//
	// }

	// public void sortColors(int[] nums) {
	//
	// int numberOfOne = 0;
	// int numberOfTwo = 0;
	// int first = 0;
	// int second = 0;
	//
	// while (first < nums.length) {
	// if (nums[first] == 0) {
	// nums[second++] = 0;
	// }
	// if (nums[first] == 1) {
	// numberOfOne++;
	// }
	// if (nums[first] == 2) {
	// numberOfTwo++;
	// }
	// first++;
	// }
	// while (numberOfOne > 0) {
	// nums[second++] = 1;
	// numberOfOne--;
	// }
	//
	// while (numberOfTwo > 0) {
	// nums[second++] = 2;
	// numberOfTwo--;
	// }
	// }
	public void sortColors(int[] nums) {

		int i = -1, j = -1;

		for (int p = 0; p < nums.length; p++) {

			int v = nums[p];
			nums[p] = 2;

			if (v == 0) {

				nums[++j] = 1;
				nums[++i] = 0;
			} else if (v == 1) {

				nums[++j] = 1;
			}
		}
	}

}
