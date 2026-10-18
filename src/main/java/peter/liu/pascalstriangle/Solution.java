package peter.liu.pascalstriangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Given numRows, generate the first numRows of Pascal's triangle.

For example, given numRows = 5,
Return

[
     [1],
    [1,1],
   [1,2,1],
  [1,3,3,1],
 [1,4,6,4,1]
]

 * 
 * @author pcnsh197
 *
 */
public class Solution {
	public List<List<Integer>> generate(int numRows) {
		if (numRows == 0) {
			return new ArrayList<>();
		}
		List<List<Integer>> result = new ArrayList<>(numRows);
		List<Integer> first = new ArrayList<>();
		first.add(1);
		result.add(first);
		if (numRows == 1) {
			return result;
		}
		for (int i = 2; i <= numRows; i++) {
			List<Integer> toAdd = new ArrayList<>(i);
			toAdd.add(1);
			for (int j = 1; j < i - 1; j++) {
				toAdd.add(first.get(j) + first.get(j - 1));
			}
			toAdd.add(1);
			result.add(toAdd);
			first = toAdd;
		}
		return result;
	}
	
	public List<List<Integer>> generateReWrite(int numRows) {
		
		List<List<Integer>> triangle = new ArrayList<>();
		if (numRows == 0) {
			return triangle;
		}

		List<Integer> firstRow = new ArrayList<>();
		firstRow.add(1);
		triangle.add(firstRow);

		for (int i = 1; i < numRows; i++) {
			List<Integer> previousRow = triangle.get(i - 1);
			List<Integer> currentRow = new ArrayList<>();
			currentRow.add(1);
			for (int j = 1; j < i; j++) {
				currentRow.add(previousRow.get(j - 1) + previousRow.get(j));
			}

			currentRow.add(1);
			triangle.add(currentRow);
		}

		return triangle;
		
	}
}
