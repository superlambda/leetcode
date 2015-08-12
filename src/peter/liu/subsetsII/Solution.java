package peter.liu.subsetsII;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Given a collection of integers that might contain duplicates, nums, return
 * all possible subsets.
 * 
 * Note: Elements in a subset must be in non-descending order. The solution set
 * must not contain duplicate subsets. For example, If nums = [1,2,2], a
 * solution is:
 * 
 * [ [2], [1], [1,2,2], [2,2], [1,2], [] ]
 * 
 * @author superlambda
 *
 */
public class Solution {
	public List<List<Integer>> subsetsWithDup(int[] nums) {
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
		Set<Integer> set = new HashSet<>();
		for (int i = 0; i < left.size(); i++) {
			List<Integer> tempLeft = new LinkedList<Integer>(left);
			List<Integer> tempCombination = new LinkedList<Integer>(combination);
			Integer toAdd = tempLeft.remove(i);
			if (!set.contains(toAdd)) {
				tempCombination.add(toAdd);
				result.add(tempCombination);
				set.add(toAdd);
				for (int j = i - 1; j >= 0; j--) {
					tempLeft.remove(0);
				}
				getCombination(result, tempCombination, tempLeft);
			}

		}
	}

}
