package inverview.nomura.aestheticallypleasing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


class SolutionTest {

	@Test
	void test() {
		Solution s = new Solution();
		int[] A = {3,4,5,3,7};
		assertEquals(s.solution(A),3);
		int[] A2 = {1,2,3,4};
		assertEquals(s.solution(A2),-1);
		int[] A3 = {1,3,1,2};
		assertEquals(s.solution(A3),0);
	}

}
