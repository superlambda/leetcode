package peter.liu.setmatrixzeroes;

import java.util.HashSet;
import java.util.Set;

/**
 * Given a m x n matrix, if an element is 0, set its entire row and column to 0.
 * Do it in place.
 * 
 * @author superlambda
 *
 */
public class Solution {
	private Set<String> indexSet = new HashSet<>();

	public void setZeroes(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == 0 && !indexSet.contains(i + "," + j)) {
					for (int column = 0; column < matrix[i].length; column++) {
						if (matrix[i][column] != 0) {
							matrix[i][column] = 0;
							indexSet.add(i + "," + column);
						}
					}
					for (int row = 0; row < matrix.length; row++) {
						if (matrix[row][j] != 0) {
							matrix[row][j] = 0;
							indexSet.add(row + "," + j);
						}
					}
				}
			}
		}

	}
}
