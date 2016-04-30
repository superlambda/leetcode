package peter.liu.wildcardmatching;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testIsMatch() {
		// assertFalse(new Solution().isMatch("aa", "a"));
		// assertTrue(new Solution().isMatch("aa", "aa"));
		// assertFalse(new Solution().isMatch("aaa", "a"));
		// assertTrue(new Solution().isMatch("aa", "*"));
		// assertTrue(new Solution().isMatch("aa", "a*"));
		// assertTrue(new Solution().isMatch("aa", "?*"));
		// assertFalse(new Solution().isMatch("aab", "c*a*b"));
		// assertFalse(new
		// Solution().isMatch("aaabbbaabaaaaababaabaaabbabbbbbbbbaabababbabbbaaaaba",
		// "a*******b"));
		// assertFalse(new
		// Solution().isMatch("babababbbabbbbaababaabbababbbbbabbabbbaaababaabbaab",
		// "**abbaa***b*ba*a*b***b*a"));
		// assertTrue(new Solution().isMatch("c", "*?*"));
		assertFalse(new Solution().isMatch("ac", "ab*"));
//		assertFalse(new Solution().isMatch(
//				"abbaaaabbbbbababbbabaaabbabbbabbbbabbaaabbabaaabbbbabbbabaaaababaaaaaaabbaabaaaabbababbabbabbaabaabaaabbababaabbabaaaaabbbaabaaaabaabababbbbbabbbbabbaabbabbabbabbaabbabababbbbabbbabbbbbbababbbabaaaabaab",
//				"****aa*****a**b***b****bab*b*baab**b*ba*a*****ba***bb**ba******a*bbb****bbb*a*****a*b*abb**aabb***aa"));

	}

}
