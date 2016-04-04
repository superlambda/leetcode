package peter.liu.longestvalidparentheses;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testLongestValidParentheses() {
//		String s="()(()";
//		String s="((()))())";
		String s=")(((((()())()()))()(()))(";
		          
		System.out.println(new Solution().longestValidParentheses(s));
	
	}
}
