package peter.liu.spiralmatrix;

import java.util.LinkedList;
import java.util.List;

/**
 * Given a matrix of m x n elements (m rows, n columns), return all elements of
 * the matrix in spiral order.
 * 
 * For example, Given the following matrix:
 * 
 * [ [ 1, 2, 3 ], 
 *   [ 4, 5, 6 ], 
 *   [ 7, 8, 9 ] ] You should return
 * [1,2,3,6,9,8,7,4,5].
 * 
 * @author superlambda
 *
 */
public class Solution {
	public List<Integer> spiralOrder(int[][] matrix) {
		List<Integer> result = new LinkedList<>();
		if(matrix.length==0){
			return result;
		}
		int minRow = 0;
		int highRow = matrix.length - 1;
		int minColumn = 0;
		int highColumn = matrix[0].length - 1;
		int total=matrix.length * matrix[0].length;
		int i = 0;
		while (i < 4) {
			switch (i) {
			case 0:
				for (int j = minColumn; j <= highColumn; j++) {
					result.add(matrix[minRow][j]);
				}
				if (result.size() == total) {
					return result;
				}
				i++;
				minRow++;
				
			case 1:
				for (int j = minRow; j <= highRow; j++) {
					result.add(matrix[j][highColumn]);
				}
				if (result.size() == total) {
					return result;
				}
				i++;
				highColumn--;
				
			case 2:
				for (int j = highColumn; j >= minColumn; j--) {
					result.add(matrix[highRow][j]);
				}
				if (result.size() == total) {
					return result;
				}
				i++;
				highRow--;
				
			case 3:
				for (int j = highRow; j >= minRow; j--) {
					result.add(matrix[j][minColumn]);
				}
				if (result.size() == total) {
					return result;
				}
				i = 0;
				minColumn++;
			}
			
		}
		return result;
	}

}
