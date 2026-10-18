package peter.liu.nextpermutation;

import java.util.Arrays;

/**
 * Implement next permutation, which rearranges numbers into the
 * lexicographically next greater permutation of numbers.
 * 
 * If such arrangement is not possible, it must rearrange it as the lowest
 * possible order (ie, sorted in ascending order).
 * 
 * The replacement must be in-place, do not allocate extra memory.
 * 
 * Here are some examples. Inputs are in the left-hand column and its
 * corresponding outputs are in the right-hand column. 1,2,3 → 1,3,2 3,2,1 →
 * 1,2,3 1,1,5 → 1,5,1
 * 
 * @author superlambda
 *
 */
public class Solution {
	public void nextPermutation(int[] num) {
		boolean found = false;
		int i = num.length - 1;

		for (; i > 0; i--) {
			if (num[i] > num[i - 1]) {
				found = true;
				break;
			}
		}
		if (!found) {
			int low = 0;
			int high = num.length - 1;
			while (low < high) {
				int temp = num[low];
				num[low] = num[high];
				num[high] = temp;
				low++;
				high--;
			}
		} else {
			int numberFound = num[i];
			int indexFound = i;
			for (int k = i + 1; k < num.length; k++) {
				if (num[k] < numberFound && num[k] > num[i - 1]) {
					indexFound = k;
					numberFound = num[k];
				}
			}
			int temp = num[i - 1];
			num[i - 1] = num[indexFound];
			num[indexFound] = temp;
			Arrays.sort(num, i, num.length);
		}

	}

}
