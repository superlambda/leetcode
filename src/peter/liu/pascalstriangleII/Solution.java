package peter.liu.pascalstriangleII;

import java.util.ArrayList;
import java.util.List;

/**
 * Given an index k, return the kth row of the Pascal's triangle.
 * 
 * For example, given k = 3, Return [1,3,3,1].
 * 
 * Note: Could you optimize your algorithm to use only O(k) extra space?
 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public List<Integer> getRow(int rowIndex) {
		rowIndex+=1;
		if (rowIndex == 0) {
			return new ArrayList<>();
		}
		List<Integer> first = new ArrayList<>();
		first.add(1);
		if (rowIndex == 1) {
			return first;
		}
		for (int i = 2; i <= rowIndex; i++) {
			List<Integer> toAdd = new ArrayList<>(i);
			toAdd.add(1);
			for (int j = 1; j < i - 1; j++) {
				toAdd.add(first.get(j) + first.get(j - 1));
			}
			toAdd.add(1);
			first = toAdd;
		}
		return first;
	}
	
	/**
	 * O(K) solution
	 * @param rowIndex
	 * @return
	 */
	public List<Integer> getRow2(int rowIndex) {
		rowIndex += 1;
		if (rowIndex == 0) {
			return new ArrayList<>();
		}
		List<Integer> first = new ArrayList<>(rowIndex);
		first.add(1);
		if (rowIndex == 1) {
			return first;
		}
		for (int i = 2; i <= rowIndex; i++) {
			for (int j = i - 2; j >= 1; j--) {
				first.set(j, first.get(j) + first.get(j - 1));
			}
			first.add(1);
		}
		return first;
	}
}
