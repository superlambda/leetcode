package peter.liu.combinationsum;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Given a set of candidate numbers (C) and a target number (T), find all unique
 * combinations in C where the candidate numbers sums to T.
 * 
 * The same repeated number may be chosen from C unlimited number of times.
 * 
 * Note: All numbers (including target) will be positive integers. Elements in a
 * combination (a1, a2, … , ak) must be in non-descending order. (ie, a1 ≤ a2 ≤
 * … ≤ ak). The solution set must not contain duplicate combinations. For
 * example, given candidate set 2,3,6,7 and target 7, A solution set is: [7] [2,
 * 2, 3]
 * 
 * @author superlambda
 *
 */
public class Solution {

	public List<List<Integer>> combinationSum(int[] candidates, int target) {
		Arrays.sort(candidates);
		List<List<Integer>> combinationList = new LinkedList<>();
		for (int i = 0; i < candidates.length; i++) {
			if (candidates[i] > target) {
				break;
			}
			int sum = 0;
			LinkedList<Integer> combination = new LinkedList<>();
			getCombination(i, sum, combination, candidates, target,
					combinationList);
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
				for (int k = index; k < candidates.length; k++) {
					if (candidates[k] <= target - sum) {
						getCombination(k, sum, new LinkedList<Integer>(
								combination), candidates, target,
								combinationList);
					}
				}
			} else if (target == sum) {
				combinationList.add(combination);
			}
		}

	}

}
