package peter.liu.validpalindrome;

import static org.junit.Assert.*;

import org.junit.Test;

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
