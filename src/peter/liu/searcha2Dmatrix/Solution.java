package peter.liu.searcha2Dmatrix;

/**
 * Write an efficient algorithm that searches for a value in an m x n matrix.
 * This matrix has the following properties:
 * 
 * Integers in each row are sorted from left to right. The first integer of each
 * row is greater than the last integer of the previous row. For example,
 * 
 * Consider the following matrix:
 * 
 * [ [1, 3, 5, 7], [10, 11, 16, 20], [23, 30, 34, 50] ] Given target = 3, return
 * true.
 * 
 * 
 * @author superlambda
 *
 */
public class Solution {
	public boolean searchMatrix(int[][] matrix, int target) {
		int rows = matrix.length;

		if (rows == 0) {
			return false;
		}
		int cols = matrix[0].length;

		if (cols == 0) {
			return false;
		}

		int low = 1;
		int high = rows * cols;

		while (low <= high) {
			int mid = low + (high - low) / 2;
			int i = (mid-1) / cols;
			int j = (mid-1) % cols;
			
			if (matrix[i][j] == target) {
				return true;
			} else if (matrix[i][j] < target) {
				low = mid + 1;
			} else {
				high = mid - 1;
			}
		}
		return false;
	}

}
