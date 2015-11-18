package peter.liu.numberofislands;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testNumIslands() {
		char[][] grid= {{'1','0','1','1','1'},{'1','0','1','0','1'},{'1','1','1','0','1'}};
		System.out.println(new Solution().numIslands(grid));
	}

}
