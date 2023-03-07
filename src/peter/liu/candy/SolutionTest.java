package peter.liu.candy;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void test() {
//		int[] ratings = {1,0,2};
		int[] ratings = {4,2,3,4,1};
		assertEquals(new Solution().candy(ratings),4);
	}

}
