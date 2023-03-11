package inverview.tesla.task1;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

class SolutionTest {

	@Test
	void test() {
		assertEquals(new Solution().solution("NAABXXAN"),1);
		assertEquals(new Solution().solution("NAANAAXNABABYNNBZ"),2);
		assertEquals(new Solution().solution("QABAAAWOBL"),0);
	}

}
