package peter.liu.validnumber;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SolutionTest {

	@Test
	public void testIsNumber() {
		assertTrue(new Solution().isNumber(" 0"));
		assertFalse(new Solution().isNumber(".e1"));
		assertFalse(new Solution().isNumber("-e58 "));
		
		
	}

}
