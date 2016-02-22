package peter.liu.regularexpressionmatching;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testIsMatch() {
//		System.out.println(new Solution().isMatch("aa", "aa"));
		
		System.out.println(new Solution().isMatch("aaa", "a*a"));
		
//		System.out.println(new Solution().isMatch("aab", "c*a*b"));
		
		
//		System.out.println(new Solution().isMatch("a", "ab*a"));
		
//		System.out.println(new Solution().isMatch("ab", ".*.."));
	}

}
