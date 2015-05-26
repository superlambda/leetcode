package peter.liu.minimumpathsum;

/**
 * Given a m x n grid filled with non-negative numbers, find a path from top
 * left to bottom right which minimizes the sum of all numbers along its path.
 * 
 * Note: You can only move either down or right at any point in time.
 * 
 * @author superlambda
 *
 */
public class Solution {
	private int[][] matrix;

	public int minPathSum(int[][] grid) {
		int m = grid.length;
		int n = grid[0].length;
		if (matrix == null) {
			matrix = new int[m][n];
			for (int i = 0; i < m; i++) {
				matrix[i] = new int[n];
			}
		}
		minPaths(grid.length, grid[0].length, grid);
		return matrix[m - 1][n - 1];
	}

	public int minPaths(int m, int n, int[][] grid) {
		if (m > 1 && n > 1) {
			if (matrix[m - 1][n - 1] == 0) {
				matrix[m - 1][n - 1] = grid[m - 1][n - 1]
						+ Math.min(minPaths(m - 1, n, grid),
								minPaths(m, n - 1, grid));
			}

			return matrix[m - 1][n - 1];
		} else if (m > 1) {
			if (matrix[m - 1][0] == 0) {
				matrix[m - 1][0] = grid[m - 1][0] + minPaths(m - 1, n, grid);
			}

			return matrix[m - 1][0];

		} else if (n > 1) {
			if (matrix[0][n - 1] == 0) {
				matrix[0][n - 1] = grid[0][n - 1] + minPaths(m, n - 1, grid);
			}

			return matrix[0][n - 1];
		} else {
			if (matrix[0][0] == 0) {
				matrix[0][0]= grid[0][0];
			}
			return matrix[0][0];
		}
	}

}
