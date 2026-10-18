package peter.liu.uniquepathsII;

/**
 * Follow up for "Unique Paths":
 * 
 * Now consider if some obstacles are added to the grids. How many unique paths
 * would there be?
 * 
 * An obstacle and empty space is marked as 1 and 0 respectively in the grid.
 * 
 * For example, There is one obstacle in the middle of a 3x3 grid as illustrated
 * below.
 * 
 * [ [0,0,0], [0,1,0], [0,0,0] ] The total number of unique paths is 2.
 * 
 * Note: m and n will be at most 100.
 * 
 * @author superlambda
 *
 */
public class Solution {
	private int[][] matrix;

	public int uniquePathsWithObstacles(int[][] obstacleGrid) {
		int m = obstacleGrid.length;
		int n = obstacleGrid[0].length;
		if (matrix == null) {
			matrix = new int[m][n];
			for (int i = 0; i < m; i++) {
				matrix[i] = new int[n];
			}
		}
		return uniquePaths(m, n, obstacleGrid);
	}

	public int uniquePaths(int m, int n, int[][] obstacleGrid) {
		if (obstacleGrid[m - 1][n - 1] == 1) {
			return 0;
		}
		if (m > 1 && n > 1) {
			if (obstacleGrid[m - 2][n - 1] == 0) {
				if (matrix[m - 2][n - 1] == 0) {
					matrix[m - 2][n - 1] = uniquePaths(m - 1, n, obstacleGrid);
				}

			}
			if (obstacleGrid[m - 1][n - 2] == 0) {
				if (matrix[m - 1][n - 2] == 0) {
					matrix[m - 1][n - 2] = uniquePaths(m, n - 1, obstacleGrid);
				}
			}
			return matrix[m - 2][n - 1] + matrix[m - 1][n - 2];
		} else if (m > 1) {
			for (int i = m - 2; i >= 0; i--) {
				if (obstacleGrid[i][0] == 1) {
					return 0;
				}
			}
			return 1;
		} else {
			for (int i = n - 2; i >= 0; i--) {
				if (obstacleGrid[0][i] == 1) {
					return 0;
				}
			}
			return 1;
		}
	}
}
