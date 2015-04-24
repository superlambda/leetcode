package peter.liu.permutationstwo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Given a collection of numbers that might contain duplicates, return all
 * possible unique permutations.
 * 
 * For example, [1,1,2] have the following unique permutations: [1,1,2],
 * [1,2,1], and [2,1,1].
 * 
 * @author superlambda
 *
 */
public class Solution {

	public List<List<Integer>> permuteUnique(int[] num) {
		List<List<Integer>> result = new LinkedList<>();
		List<Integer> list = new ArrayList<>();
		for (int number : num) {
			list.add(number);
		}
		getNexPermutation(result, new LinkedList<Integer>(), list, list.size());
		return result;
	}

	private void getNexPermutation(List<List<Integer>> result,
			List<Integer> pList, List<Integer> leftList, int length) {
		Set<Integer> set = new HashSet<>();
		for (int i = 0; i < leftList.size(); i++) {
			if (!set.contains(leftList.get(i))) {
				List<Integer> ppList = new LinkedList<>(pList);
				ppList.add(leftList.get(i));
				if(ppList.size()<length){
					List<Integer> lleftList = new LinkedList<>(leftList);
					lleftList.remove(i);
					getNexPermutation(result, ppList, lleftList, length);
				}else{
					result.add(ppList);
				}
				set.add(leftList.get(i));
			}
		}
	}
}
