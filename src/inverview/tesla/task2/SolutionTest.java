package inverview.tesla.task2;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

class SolutionTest {

	@Test
	void test() {
		assertEquals(new Solution().solution("aabab"),3);
		assertEquals(new Solution().solution("dog"),8);
		assertEquals(new Solution().solution("aa"),0);
		assertEquals(new Solution().solution("baaaa"),-1);
	}

}
