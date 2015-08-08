package peter.liu.subsets;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Given a set of distinct integers, nums, return all possible subsets.
 * 
 * Note: Elements in a subset must be in non-descending order. The solution set
 * must not contain duplicate subsets. For example, If nums = [1,2,3], a
 * solution is:
 * 
 * [ [3], [1], [2], [1,2,3], [1,3], [2,3], [1,2], [] ]
 * 
 * @author superlambda
 *
 */
public class Solution {

	public List<List<Integer>> subsets(int[] nums) {
		Arrays.sort(nums);
		List<Integer> left = new LinkedList<>();
		List<List<Integer>> result = new LinkedList<List<Integer>>();
		for (int i : nums) {
			left.add(i);
		}
		getCombination(result, new LinkedList<Integer>(), left);
		result.add(new LinkedList<Integer>());
		return result;
	}

	private void getCombination(List<List<Integer>> result,
			List<Integer> combination, List<Integer> left) {
		for (int i = 0; i < left.size(); i++) {
			List<Integer> tempLeft = new LinkedList<Integer>(left);
			List<Integer> tempCombination = new LinkedList<Integer>(combination);
			tempCombination.add(tempLeft.remove(i));
			result.add(tempCombination);
			for (int j = i - 1; j >= 0; j--) {
				tempLeft.remove(0);
			}
			getCombination(result, tempCombination, tempLeft);
		}
	}
}
