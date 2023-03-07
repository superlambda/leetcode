package inverview.nomura.task2;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

class SolutionTest {

	@Test
	void test() {
		assertEquals(new Solution().solution("abacdec"),3);
		assertEquals(new Solution().solution("world"),1);
		assertEquals(new Solution().solution("dddd"),4);
		assertEquals(new Solution().solution("cycle"),2);
		assertEquals(new Solution().solution("abba"),2);
	}

}
