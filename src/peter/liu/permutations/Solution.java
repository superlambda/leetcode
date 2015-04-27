package peter.liu.permutations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		List<List<Integer>> result = new ArrayList<>();
		List<Integer> list = new ArrayList<>();
		for (int number : num) {
			list.add(number);
		}
		getNexPermutation(result, new ArrayList<Integer>(list.size()), list,
				num.length);
		return result;
	}

	private void getNexPermutation(List<List<Integer>> result,
			List<Integer> pList, List<Integer> leftList, int length) {
		Set<Integer> set = new HashSet<>();
		for (int i = 0; i < leftList.size(); i++) {
			if (!set.contains(leftList.get(i))) {
				List<Integer> ppList = new ArrayList<>(pList);
				ppList.add(leftList.get(i));
				if (ppList.size() < length) {
					List<Integer> lleftList = new ArrayList<>(leftList);
					lleftList.remove(i);
					getNexPermutation(result, ppList, lleftList, length);
				} else {
					result.add(ppList);
				}
				set.add(leftList.get(i));
			}
		}
	}
}
