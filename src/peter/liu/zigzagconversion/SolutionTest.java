package peter.liu.zigzagconversion;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testConvert() {
		assertEquals(new Solution().convert("PAYPALISHIRING", 3),"PAHNAPLSIIGYIR");
		assertEquals(new Solution().convert("ABC", 2),"ACB");
		assertEquals(new Solution().convert("AB", 1),"AB");
		assertEquals(new Solution().convert("ABC", 1),"ABC");
		assertEquals(new Solution().convert("ABCD", 2),"ACBD");
	}

}
