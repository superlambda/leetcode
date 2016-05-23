package peter.liu.validnumber;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testIsNumber() {
		assertTrue(new Solution().isNumber(" 0"));
		assertFalse(new Solution().isNumber(".e1"));
		assertFalse(new Solution().isNumber("-e58 "));
		
		
	}

}
