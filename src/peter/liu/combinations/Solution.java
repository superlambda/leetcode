package peter.liu.combinations;

import java.util.LinkedList;
import java.util.List;

/**
 * Given two integers n and k, return all possible combinations of k numbers out
 * of 1 ... n.
 * 
 * For example, If n = 4 and k = 2, a solution is:
 * 
 * [ [2,4], [3,4], [2,3], [1,2], [1,3], [1,4], ]
 * 
 * @author superlambda
 *
 */
public class Solution {

	private int k = 0;

	public List<List<Integer>> combine(int n, int k) {
		if (n < k) {
			return new LinkedList<List<Integer>>();
		}
		this.k = k;
		List<List<Integer>> result = new LinkedList<List<Integer>>();
		List<Integer> left = new LinkedList<>();
		for (int i = 1; i <= n; i++) {
			left.add(i);
		}
		getCombination(result, new LinkedList<Integer>(), left);
		return result;
	}

	private void getCombination(List<List<Integer>> result,
			List<Integer> combination, List<Integer> left) {
		int totalSize = combination.size() + left.size();
		if (totalSize == k) {
			combination.addAll(left);
			result.add(combination);

		} else if (combination.size() < k && totalSize > k) {
			int leftToAdd = left.size() - (k - combination.size()) + 1;
			for (int i = 0; i < leftToAdd; i++) {
				List<Integer> tempLeft = new LinkedList<Integer>(left);
				List<Integer> tempCombination = new LinkedList<Integer>(
						combination);
				tempCombination.add(tempLeft.remove(i));
				if (tempCombination.size() == k) {
					result.add(tempCombination);
				} else {
					for (int j = i - 1; j >= 0; j--) {
						tempLeft.remove(0);
					}
					getCombination(result, tempCombination, tempLeft);
				}

			}

		}
	}

}
