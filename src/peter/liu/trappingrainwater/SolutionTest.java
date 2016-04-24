package peter.liu.trappingrainwater;

import org.junit.Test;

public class SolutionTest {
	
//	int[] height = {0,1,0,2,1,0,1,3,2,1,2,1 };
	int[] height = {4,2,3};
	@Test
	public void testTrap() {
		
		System.out.println("result: " + new Solution().trap(height));
	}

}
