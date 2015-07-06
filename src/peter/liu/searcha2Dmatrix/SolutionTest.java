package peter.liu.searcha2Dmatrix;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testSearchMatrix() {
		int[][] matrix={{1,1}};
		assertFalse(new Solution().searchMatrix(matrix, 0));
		int[][] matrix2={{1},{3}};
		assertFalse(new Solution().searchMatrix(matrix2, 0));
		assertTrue(new Solution().searchMatrix(matrix2, 3));
		int[][] matrix3={{1}};
		assertTrue(new Solution().searchMatrix(matrix3, 1));	
		int[][] matrix4={{1,3}};
		assertTrue(new Solution().searchMatrix(matrix4, 3));
		
		int[][] matrix5={{1,3,5,7},{10,11,16,20},{23,30,34,50}};
		assertTrue(new Solution().searchMatrix(matrix5, 30));
	}

}
