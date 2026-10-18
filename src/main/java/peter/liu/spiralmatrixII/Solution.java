package peter.liu.spiralmatrixII;

/**
 * Given an integer n, generate a square matrix filled with elements from 1 to
 * n2 in spiral order.
 * 
 * For example, Given n = 3,
 * 
 * You should return the following matrix: [ [ 1, 2, 3 ], [ 8, 9, 4 ], [ 7, 6, 5
 * ] ]
 * 
 * @author superlambda
 *
 */
public class Solution {
	public int[][] generateMatrix(int n) {
		int[][] matrix = new int[n][n];
		int minRow = 0;
		int highRow = n - 1;
		int minColumn = 0;
		int highColumn = n - 1;
		int total = n * n;
		int i = 0;
		int value = 1;
		while (i < 4) {
			switch (i) {
			case 0:
				for (int j = minColumn; j <= highColumn; j++) {
					matrix[minRow][j] = value++;
				}
				if (value > total) {
					return matrix;
				}
				i++;
				minRow++;

			case 1:
				for (int j = minRow; j <= highRow; j++) {
					matrix[j][highColumn] = value++;
				}
				if (value > total) {
					return matrix;
				}
				i++;
				highColumn--;

			case 2:
				for (int j = highColumn; j >= minColumn; j--) {
					matrix[highRow][j] = value++;
				}
				if (value > total) {
					return matrix;
				}
				i++;
				highRow--;

			case 3:
				for (int j = highRow; j >= minRow; j--) {
					matrix[j][minColumn] = value++;
				}
				if (value > total) {
					return matrix;
				}
				i = 0;
				minColumn++;
			}

		}
		return matrix;

	}

}
