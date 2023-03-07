package inverview.nomura.directedgraph;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SolutionTest {

	@Test
	void test() {
		Solution s = new Solution();
		int A[] = {3,1,2};
		int B[] = {2,3,1};
		assertEquals(s.solution(A,B),true);
		
		int[] A2 = {1,2,1};
		int[] B2 = {2,3,3};
		assertEquals(s.solution(A2,B2),false);
		

		int[] A3 = {1,2,3,4};
		int[] B3 = {2,1,4,4};
		assertEquals(s.solution(A3,B3),false);
		
		int[] A4 = {1,2,3,4};
		int[] B4 = {2,1,4,3};
		assertEquals(s.solution(A4,B4),false);
		
		int[] A5 = {1,2,2,3,3};
		int[] B5 = {2,3,3,4,5};
		assertEquals(s.solution(A5,B5),false);
		
		int A6[] = {3,2,1};
		int B6[] = {2,1,2};
		assertEquals(s.solution(A6,B6),false);
	}

}
