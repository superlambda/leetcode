package peter.liu.firstbadversion;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SolutionTest {

	@Test
	void test() {
		assertEquals(11, new Solution(11).firstBadVersion(15));
		assertEquals(1702766719, new Solution(1702766719).firstBadVersion(2126753390));

	}
}
