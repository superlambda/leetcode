package peter.liu.permutations;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Given a collection of numbers, return all possible permutations.
 * 
 * For example, [1,2,3] have the following permutations: [1,2,3], [1,3,2],
 * [2,1,3], [2,3,1], [3,1,2], and [3,2,1].
 * 
 * @author superlambda
 *
 */
public class Solution {
	public List<List<Integer>> permute(int[] num) {
		List<List<Integer>> result = new LinkedList<>();
		List<Integer> list = new LinkedList<>();
		Integer[] num2 = new Integer[num.length];
		for (int i = 0; i < num.length; i++) {
			list.add(num[i]);
			num2[i] = num[i];
		}
		result.add(list);

		getNextGreaterPermutation(result, num);
		getNextSmallerPermutation(result, num2);
		return result;
	}

	private void getNextGreaterPermutation(List<List<Integer>> result, int[] num) {

		int i = num.length - 1;
		boolean found = false;
		for (; i > 0; i--) {
			if (num[i] > num[i - 1]) {
				found = true;
				break;
			}
		}
		if (!found) {
			return;
		}
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
		List<Integer> list = new LinkedList<>();
		for (int number : num) {
			list.add(number);
		}
		result.add(list);
		getNextGreaterPermutation(result, num);
	}

	private void getNextSmallerPermutation(List<List<Integer>> result,
			Integer[] num) {

		int i = num.length - 1;
		boolean found = false;
		for (; i > 0; i--) {
			if (num[i] < num[i - 1]) {
				found = true;
				break;
			}
		}
		if (!found) {
			return;
		}
		int numberFound = num[i];
		int indexFound = i;
		for (int k = i + 1; k < num.length; k++) {
			if (num[k] > numberFound && num[k] < num[i - 1]) {
				indexFound = k;
				numberFound = num[k];
			}
		}
		int temp = num[i - 1];
		num[i - 1] = num[indexFound];
		num[indexFound] = temp;
		Arrays.sort(num, i, num.length, (Integer a, Integer b) -> {
			return b - a;
		});
		List<Integer> list = new LinkedList<>();
		for (int number : num) {
			list.add(number);
		}
		result.add(list);
		getNextSmallerPermutation(result, num);
	}

}
