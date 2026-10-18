package peter.liu.uniquepaths;

/**
 * A robot is located at the top-left corner of a m x n grid (marked 'Start' in
 * the diagram below).
 * 
 * The robot can only move either down or right at any point in time. The robot
 * is trying to reach the bottom-right corner of the grid (marked 'Finish' in
 * the diagram below).
 * 
 * How many possible unique paths are there?
 * 
 * @author superlambda
 *
 */
public class Solution {
	private int[][] matrix;

	public int uniquePaths(int m, int n) {
		if (matrix == null) {
			matrix = new int[m][n];
			for (int i = 0; i < m; i++) {
				matrix[i] = new int[n];
			}
		}
		if (m > 1 && n > 1) {
			if (matrix[m - 2][n - 1] == 0) {
				matrix[m - 2][n - 1] = uniquePaths(m - 1, n);
			}
			if (matrix[m - 1][n - 2] == 0) {
				matrix[m - 1][n - 2] = uniquePaths(m, n - 1);
			}
			return matrix[m - 2][n - 1] + matrix[m - 1][n - 2];
		} else {
			return 1;
		}
	}

}
