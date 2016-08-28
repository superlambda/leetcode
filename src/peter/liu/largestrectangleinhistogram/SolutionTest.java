package peter.liu.largestrectangleinhistogram;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testLargestRectangleArea() {
		int[] heights = { 2, 1, 5, 6, 2, 3 };
		assertEquals(new Solution().largestRectangleArea(heights), 10);

		int[] heights2 = { 1, 2, 2 };
		assertEquals(new Solution().largestRectangleArea(heights2), 4);

		int[] heights3 = { 1, 2, 3, 4, 5 };
		assertEquals(new Solution().largestRectangleArea(heights3), 9);

		int[] heights4 = { 1, 3, 2, 1, 2, 1 };
		assertEquals(new Solution().largestRectangleArea(heights4), 6);

		int[] heights5 = { 3, 5, 5, 2, 5, 5, 6, 6, 4, 4, 1, 1, 2, 5, 5, 6, 6, 4, 1, 3 };
		assertEquals(new Solution().largestRectangleArea(heights5), 24);
		
		int[] heights6 = { 6, 4, 2, 0, 3, 2, 0, 3, 1, 4, 5, 3, 2, 7, 5, 3, 0, 1, 2, 1, 3, 4, 6, 8, 1, 3 };
		assertEquals(new Solution().largestRectangleArea(heights6), 14);
		
		int[] heights7={1,8,9,7,8,8,4,7,9,1,8,2,4,8,4,0,5};//
		assertEquals(new Solution().largestRectangleArea(heights7), 35);
		
		
		int[] heights8={5,5,1,7,1,1,5,2,7,6};//
		assertEquals(new Solution().largestRectangleArea(heights8), 12);
	
	}

}
