package peter.liu.multiplystrings;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SolutionTest {

	@Test
	public void testMultiply() {
		assertEquals(new Solution().multiply("2", "3"), "6");
		assertEquals(new Solution().multiply("123", "456"), "56088");
		
	}

}
