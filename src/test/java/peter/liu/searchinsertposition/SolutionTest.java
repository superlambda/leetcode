package peter.liu.searchinsertposition;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SolutionTest {

	@Test
	public void testSearchInsert() {
		int[] num = { 1, 3 };
		assertEquals(new Solution().searchInsert(num, 2), 1);
	}

}
