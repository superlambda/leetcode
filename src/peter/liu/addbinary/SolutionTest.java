package peter.liu.addbinary;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testSortList() {
		Solution s = new Solution();
		assertEquals(s.addBinary("0", "1"),"1");
	}

}
