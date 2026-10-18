package peter.liu.candy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SolutionTest {

	@Test
	public void test() {
//		int[] ratings = {1,0,2};
		int[] ratings = {4,2,3,4,1};
		assertEquals(9, new Solution().candy(ratings));
	}

}
