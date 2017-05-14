package peter.liu.maximalrectangle;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void test() {
		char[][] matrix = { { '1', '0', '1', '0', '0' }, 
							{ '1', '0', '1', '1', '1' }, 
							{ '1', '1', '1', '1', '1' },
						    { '1', '0', '0', '1', '0' } };
		
		assertEquals(new Solution().maximalRectangle(matrix),6);
	}

}
