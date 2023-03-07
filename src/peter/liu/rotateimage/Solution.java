package peter.liu.rotateimage;

/**
 * You are given an n x n 2D matrix representing an image.
 * 
 * Rotate the image by 90 degrees (clockwise).
 * 
 * Follow up: Could you do this in-place?
 * 
 * @author superlambda
 *
 */
public class Solution {
//	public void rotate(int[][] matrix) {
//		if (matrix[0].length == 1 && matrix.length == 1) {
//			return;
//		}
//		int[][] temp = new int[matrix.length][matrix[0].length];
//		for (int i = 0; i < matrix.length; i++) {
//			for (int j = 0; j < matrix[i].length; j++) {
//				temp[j][matrix[0].length - 1 - i] = matrix[i][j];
//			}
//		}
//		
//		for (int i = 0; i < matrix.length; i++) {
//			for (int j = 0; j < matrix[i].length; j++) {
//				matrix[i][j]=temp[i][j];
//			}
//		}
//	}
	
	public void rotate(int[][] matrix) {
		
		int length = matrix.length;
		
        for(int i=0; i<length; i++) {
			for(int j=i;j<length;j++) {
				int temp=matrix[i][j];
				matrix[i][j]=matrix[j][i];
				matrix[j][i]=temp;
			}
		}
        for(int i =0; i< length; i++) {
        	for(int j=0;j<length/2; j++) {
        		int temp=matrix[i][j];
        		matrix[i][j]=matrix[i][length-1-j];
        		matrix[i][length-1-j]=temp;
        	}
        }
    }

}
