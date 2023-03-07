package inverview.nomura.balancedstring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SolutionTest {

	@Test
	void test() {
		
		Solution s = new Solution();
		assertEquals(s.solution("azABaabza"),5);
		assertEquals(s.solution("TacoCat"),-1);
		assertEquals(s.solution("AcZCbaBz"),8);
		assertEquals(s.solution("abcdefghijklmnopqrstuvwxyz"),-1);
	}

}
