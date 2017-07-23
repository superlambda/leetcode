package peter.liu.interleavingstring;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testIsInterleave() {
		assertTrue(new Solution().isInterleave("aabcc", "dbbca", "aadbbcbcac"));

		assertFalse(new Solution().isInterleave("bcbccabcccbcbbbcbbacaaccccacbaccabaccbabccbabcaabbbccbbbaa",
				"ccbccaaccabacaabccaaccbabcbbaacacaccaacbacbbccccbac",
				"bccbcccabbccaccaccacbacbacbabbcbccbaaccbbaacbcbaacbacbaccaaccabcaccacaacbacbacccbbabcccccbababcaabcbbcccbbbaa"));
		// assertFalse(new Solution().isInterleave("aabcc", "dbbca",
		// "aadbbbaccc"));
	}

}
