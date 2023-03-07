package inverview.nomura.maxtimeslot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SolutionTest {

	@Test
	void test() {
		Solution s = new Solution();
		assertEquals(s.solution("Mon 10:00-11:00\nMon 13:14-15:30\nTue 19:00-20:00"),136);
	}

}
