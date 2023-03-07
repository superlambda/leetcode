package inverview.nomura.sumzero;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SolutionTest {

	@Test
	void test() {
		Solution s = new Solution();
		int A[] = {2,-2,3,0,4,-7};
		assertEquals(s.solution(A),4);
		
		int B[]= new int[100000];
		assertEquals(s.solution(B),-1);
		
	}

}
