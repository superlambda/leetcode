package peter.liu.combinationsumtwo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Given a collection of candidate numbers (C) and a target number (T), find all
 * unique combinations in C where the candidate numbers sums to T.
 * 
 * Each number in C may only be used once in the combination.
 * 
 * Note: All numbers (including target) will be positive integers. Elements in a
 * combination (a1, a2, … , ak) must be in non-descending order. (ie, a1 ≤ a2 ≤
 * … ≤ ak). The solution set must not contain duplicate combinations. For
 * example, given candidate set 10,1,2,7,6,1,5 and target 8, A solution set is:
 * [1, 7] [1, 2, 5] [2, 6] [1, 1, 6]
 * 
 * @author superlambda
 *
 */
public class Solution {

	public List<List<Integer>> combinationSum2(int[] num, int target) {
		Arrays.sort(num);
		List<List<Integer>> combinationList = new LinkedList<>();
		for (int i = 0; i < num.length; i++) {
			if (num[i] > target) {
				break;
			}
			if (i == 0 || num[i] != num[i - 1]) {
				int sum = 0;
				LinkedList<Integer> combination = new LinkedList<>();
				getCombination(i, sum, combination, num, target,
						combinationList);
			}
		}
		return combinationList;
	}

	private void getCombination(int index, int sum,
			LinkedList<Integer> combination, int[] candidates, int target,
			List<List<Integer>> combinationList) {
		sum += candidates[index];
		if (target >= sum) {
			combination.add(candidates[index]);
			if (target > sum) {
				for (int k = index + 1; k < candidates.length; k++) {
					if (candidates[k] <= target - sum) {
						if (k == index + 1
								|| candidates[k] != candidates[k - 1]) {
							getCombination(k, sum, new LinkedList<Integer>(
									combination), candidates, target,
									combinationList);
						}
					}
				}
			} else if (target == sum) {
				combinationList.add(combination);
			}
		}
	}

}
