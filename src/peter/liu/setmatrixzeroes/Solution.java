package peter.liu.setmatrixzeroes;

import java.util.HashSet;
import java.util.Set;

/**
 * Given a m x n matrix, if an element is 0, set its entire row and col to 0.
 * Do it in place.
 * 
 * @author superlambda
 *
 */
public class Solution {
	private Set<String> indexSet = new HashSet<>();

	// public void setZeroes(int[][] matrix) {
	// for (int i = 0; i < matrix.length; i++) {
	// for (int j = 0; j < matrix[i].length; j++) {
	// if (matrix[i][j] == 0 && !indexSet.contains(i + "," + j)) {
	// for (int col = 0; col < matrix[i].length; col++) {
	// if (matrix[i][col] != 0) {
	// matrix[i][col] = 0;
	// indexSet.add(i + "," + col);
	// }
	// }
	// for (int row = 0; row < matrix.length; row++) {
	// if (matrix[row][j] != 0) {
	// matrix[row][j] = 0;
	// indexSet.add(row + "," + j);
	// }
	// }
	// }
	// }f
	// }
	//
	// }
	/**
	 * O(m+n)
	 * @param matrix
	 */
	public void setZeroes(int[][] matrix) {
		int[] rowCheck = new int[matrix.length];
		int[] colCheck = new int[matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == 0) {
					if (rowCheck[i] == 0) {
						rowCheck[i] = 1;
					}
					if (colCheck[j] == 0) {
						colCheck[j] = 1;
					}
				}
			}
		}
		for (int i = 0; i < rowCheck.length; i++) {
			if (rowCheck[i] == 1) {
				for (int col = 0; col < matrix[i].length; col++) {
					if (matrix[i][col] != 0) {
						matrix[i][col] = 0;
					}
				}
			}
		}
		for (int j = 0; j < colCheck.length; j++) {
			if (colCheck[j] == 1) {
				for (int row = 0; row < matrix.length; row++) {
					if (matrix[row][j] != 0) {
						matrix[row][j] = 0;
					}
				}
			}
		}
	}
}
