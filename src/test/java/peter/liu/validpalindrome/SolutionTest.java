package peter.liu.validpalindrome;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author pcnsh197
 *
 */
public class SolutionTest {

	@Test
	public void testIsPalindrome() {
		assertEquals(
				new Solution().isPalindrome("A man, a plan, a canal: Panama"),
				true);

		assertEquals(
				new Solution().isPalindrome(" "),
				true);
		assertEquals(
				new Solution().isPalindrome("0P"),
				false);
		
		
	}
}
